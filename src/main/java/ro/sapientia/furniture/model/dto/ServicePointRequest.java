package ro.sapientia.furniture.model.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class ServicePointRequest {
    private Long id;
    private Long regionId;
    private String country;
    private String county;
    private String city;
    private String street;
    private String number;
    private String zipCode;

    public Long id() {
        return id;
    }

    public Long regionId() {
        return regionId;
    }

    public String country() {
        return country;
    }

    public String county() {
        return county;
    }

    public String city() {
        return city;
    }

    public String street() {
        return street;
    }

    public String number() {
        return number;
    }

    public String zipCode() {
        return zipCode;
    }
}
