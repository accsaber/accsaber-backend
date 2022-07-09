package de.ixsen.accsaber.business.exceptions

enum class ExceptionType(val errorCode: String) {
    PLAYER_ALREADY_EXISTS("00001"), RANKED_MAP_ALREADY_EXISTS("00002"), CATEGORY_ALREADY_EXISTS("00003"), PLAYER_NOT_FOUND("00100"), RANKED_MAP_NOT_FOUND("00101"), CATEGORY_NOT_FOUND(
        "00102"
    ),
    AUTH_INVALID("10000");

}