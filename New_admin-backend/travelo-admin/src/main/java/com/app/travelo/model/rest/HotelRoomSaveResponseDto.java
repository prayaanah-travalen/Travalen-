package com.app.travelo.model.rest;

public class HotelRoomSaveResponseDto {
	private Long hotelRoomId;
    private Long hotelCode;
    private String message;

    public HotelRoomSaveResponseDto() {}

    public Long getHotelRoomId() {
        return hotelRoomId;
    }

    public void setHotelRoomId(Long hotelRoomId) {
        this.hotelRoomId = hotelRoomId;
    }

    public Long getHotelCode() {
        return hotelCode;
    }

    public void setHotelCode(Long hotelCode) {
        this.hotelCode = hotelCode;
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }
}
