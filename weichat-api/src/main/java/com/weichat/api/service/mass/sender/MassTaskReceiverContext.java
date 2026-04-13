package com.weichat.api.service.mass.sender;

public class MassTaskReceiverContext {

    private final String senderUuid;
    private final Long receiverUserId;
    private final boolean roomMessage;
    private final String receiverName;

    public MassTaskReceiverContext(String senderUuid, Long receiverUserId, boolean roomMessage, String receiverName) {
        this.senderUuid = senderUuid;
        this.receiverUserId = receiverUserId;
        this.roomMessage = roomMessage;
        this.receiverName = receiverName;
    }

    public String getSenderUuid() {
        return senderUuid;
    }

    public Long getReceiverUserId() {
        return receiverUserId;
    }

    public boolean isRoomMessage() {
        return roomMessage;
    }

    public String getReceiverName() {
        return receiverName;
    }
}
