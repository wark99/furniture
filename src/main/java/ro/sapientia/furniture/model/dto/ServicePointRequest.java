package ro.sapientia.furniture.model.dto;

public record ServicePointRequest(
    Long regionId,
    String country,
    String county,
    String city,
    String street,
    String number,
    String zipCode
) {
}
