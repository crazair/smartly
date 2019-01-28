package ru.sbtqa.smartly.dataobjects;

/** Класс для хранениия иденитификаторов сущности в Grid */
public class GridGainId {

    private long targetId;

    private long parentId;


    //не используется оставлен для совместимости с модулем миграции
    private long partitionId = 1L;

    public long getTargetId() {
        return targetId;
    }

    public void setTargetId(long targetId) {
        this.targetId = targetId;
    }

    public long getParentId() {
        return parentId;
    }

    public void setParentId(long parentId) {
        this.parentId = parentId;
    }

    public long getPartitionId() {
        return partitionId;
    }

    public void setPartitionId(long partitionId) {
        this.partitionId = partitionId;
    }

}
