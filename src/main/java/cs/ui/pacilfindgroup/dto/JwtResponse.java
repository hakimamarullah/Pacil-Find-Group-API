package cs.ui.pacilfindgroup.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class JwtResponse {
    private String username;
    private boolean isSuperuser;
    private String jwtToken;
}
