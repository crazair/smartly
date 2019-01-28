package ru.sbtqa.smartly.common.utils;

import com.codeborne.selenide.Selenide;
import com.jcraft.jsch.*;
import java.io.IOException;
import java.io.InputStream;
import static ru.sbtqa.smartly.common.utils.PropertyUtils.LOG;
import static ru.sbtqa.smartly.common.utils.PropertyUtils.prop;

/**
 * Утилитный класс для работы по ssh с удаленными серверами
 */

public class SSHUtils {

    private static final int SSH_PORT = Integer.parseInt(prop("sshPort"));
    private static final String HOSTNAME = prop("sshHost");
    private static final String USERNAME = prop("sshLogin");
    private static final String PASSWORD = prop("sshPassword");
    private static final int N_LINES_FROM_LOG = 5000; //желаемое количество последних сток лог файла
    private static final String COMMANDS = "tail -n " + N_LINES_FROM_LOG + " " + prop("remoteLog");
    private static final int BUFFER_SIZE = 2097152;
    private static final int CONNECTION_TIMEOUT = N_LINES_FROM_LOG * 1000 / (BUFFER_SIZE / 1000);

    /**
     * Метод для получения n последних строк файла с удаленного сервера в виде строки
     **/
    public static String getTailOfRemoteLog() {
        JSch jsch = new JSch();
        Session session = null;
        String dataFromChannel = null;
        if (!HOSTNAME.equals(StringUtils.ZERO_IP)) {
            try {
                session = jsch.getSession(USERNAME, HOSTNAME, SSH_PORT);
                session.setConfig("StrictHostKeyChecking", "no");
                session.setConfig("max_input_buffer_size", String.valueOf(BUFFER_SIZE));
                session.setPassword(PASSWORD);
                LOG.info("Создание ssh-сессии c удаленным сервером " + HOSTNAME);
                session.connect(CONNECTION_TIMEOUT);

                //выполняем команды в удаленном шелле
                Channel channel = session.openChannel("exec");
                channel.run();
                ChannelExec channelExec = (ChannelExec) channel;
                InputStream in = channel.getInputStream();
                channelExec.setCommand(COMMANDS);
                channelExec.setInputStream(null);
                channelExec.setErrStream(System.err);
                LOG.info("Создание канала для выполнения команд на удаленном сервере " + HOSTNAME);
                channel.connect();
                LOG.info("Получение данных с удаленного сервера " + HOSTNAME);
                dataFromChannel = getDataFromChannel(channel, in);
                channel.disconnect();
                return dataFromChannel;
            } catch (JSchException e) {
                LOG.error("Ошибка при доступе по ssh к удаленному серверу: ", e);
            } catch (IOException e) {
                LOG.error("Ошибка чтения из потока данных удаленного сервера: ", e);
            }
        } else {
            LOG.info("Не удалось извлечь ip-адрес хоста из url-строки" + prop("url"));
            LOG.info("Лог-файл сервера приложений не будет прикреплен к отчёту.");
        }
        return dataFromChannel;
    }

    /**Метод для получения данных из канала ssh-сессии
     *  Здесь мы считываем данные в буфер указанного нами размера до тех пор, пока данные продолжают поступать.
     *  После закрытия канала получаем код статуса. Если всё прошло успешно, статус равен нулю.
     *  Пока канал не закрыт, ожидаем получения новых данных с интервалом в одну секунду - особенности реализации
     *  родительского класса */
    private static String getDataFromChannel(Channel channel, InputStream in) throws IOException {
        StringBuilder result = new StringBuilder();
        int sleepTimeOut = 1000;
        byte[] tmp = new byte[BUFFER_SIZE];
        while (true) {
            while (in.available() > 0) {
                int i = in.read(tmp, 0, BUFFER_SIZE);
                if (i < 0) {
                    break;
                }
                result.append(new String(tmp, 0, i));
                LOG.info("Получена порция данных объёмом " + i + " байт");
            }
            if (channel.isClosed()) {
                int exitStatus = channel.getExitStatus();
                LOG.info("Статус закрыти канала exec удаленного сервера: " + exitStatus);
                break;
            }
            LOG.info("Ожидание следующей порции данных (" + sleepTimeOut + " милисекунд)...");
            Selenide.sleep(sleepTimeOut);
        }
        return result.toString();
    }
}
