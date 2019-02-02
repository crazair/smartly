declare
  VTBL     "CONSTANT".NUMBER_TABLE;
  lck      integer;
  prod_ID  Z#PCL_IN_REQUEST.ID%TYPE;
begin
  -- открываем сессию RTL
  lck := EXECUTOR.LOCK_OPEN;
  VTBL.DELETE;
  -- здесь ищем нуждую запись и получаем ее идентификатор
  SELECT MIN(ID) INTO prod_ID FROM Z#PCL_IN_REQUEST WHERE C_RQUID = '<rquid>';
  VTBL(1) := prod_ID;
  -- теперь дергаем ее обработку
  Z$PCL_IN_REQUEST_PROC.PROC_EXECUTE(VTBL,NULL);
  -- закрываем сессию RTL
  EXECUTOR.LOCK_CLOSE;
end;