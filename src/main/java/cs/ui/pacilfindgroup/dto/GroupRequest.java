package cs.ui.pacilfindgroup.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class GroupRequest {
    private String username;
    private String courseId;
    private String kelas;
    private Integer maxMember;
}
