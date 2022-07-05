package cs.ui.pacilfindgroup.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;


@Data
@NoArgsConstructor
@AllArgsConstructor
public class UserDTO {
    private Long npm;
    private String email;
    private String lineID;
    private String password;
    private boolean isSuperuser;
}
