package io.swagger.model.DTO;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class UserFilterDTO {

    private Integer offset;
    private Integer limit;

    private String firstname;
    private String lastname;

    private boolean accountFilterEnable;
    private boolean activatedFilterEnable;

    private boolean hasAccount;
    private boolean activated;


}
