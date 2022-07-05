package cs.ui.pacilfindgroup.dto;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertDoesNotThrow;
import static org.junit.jupiter.api.Assertions.assertEquals;

class AcceptorDeclineApplicationDTOTest {

    private AcceptorDeclineApplicantDTO acceptorDeclineApplicantDTO;

    @BeforeEach
    void setUp() {
        acceptorDeclineApplicantDTO = new AcceptorDeclineApplicantDTO();
        acceptorDeclineApplicantDTO.setTypeId(2);

    }

    @Test
    void testGetMethod() {
        assertEquals(2, acceptorDeclineApplicantDTO.getTypeId());
    }

    @Test
    void testAcceptorDeclineApplicantDTOShouldHaveAttributeTypeId() throws NoSuchFieldException {
        assertDoesNotThrow(() -> AcceptorDeclineApplicantDTO.class.getDeclaredField("typeId"));
        assertEquals(int.class, AcceptorDeclineApplicantDTO.class.getDeclaredField("typeId").getType());
    }


}