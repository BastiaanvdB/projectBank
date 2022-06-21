package io.swagger.model.DTO;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;

@AllArgsConstructor
@Getter
@Setter
public class AccountFilterDTO {

    private int offset;
    private int limit;
    private String firstname;
    private String lastname;

    public boolean hasFirstnameFilter() {
        return (firstname != null && !firstname.isEmpty()) && (lastname == null || lastname.isEmpty());
    }

    public boolean hasLastnameFilter() {
        return (firstname == null || firstname.isEmpty()) && (lastname != null && !lastname.isEmpty());
    }

    public boolean hasBothFilters() {
        return (firstname != null && !firstname.isEmpty()) && (lastname != null && !lastname.isEmpty());
    }
}
