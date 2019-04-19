//given
import org.junit.jupiter.api.BeforeEach;
//given
import org.junit.jupiter.api.DisplayName;
//given
import org.junit.jupiter.api.Nested;
//given
import org.junit.jupiter.api.Test;
//given
import static org.junit.jupiter.api.Assertions.assertEquals;
//given
import static org.junit.jupiter.api.Assertions.assertFalse;
//given
import static org.junit.jupiter.api.Assertions.assertTrue;

//given
@DisplayName("Tests for GymAPI class")
public class GymAPITest {
    //given
    GymAPI gymAPI;
    //given
    Member member1;
    //given
    Member member2;
    //given
    Member member3;
    //given
    Trainer trainer1;
    //given
    Trainer trainer2;
    //given
    Trainer trainer3;

    //given
    @BeforeEach
    //given
    public void setUp() {
        //given
        gymAPI = new GymAPI();
        //given
        member1 = new Member("email1", "name1", "address1", "F",
                 1.5f, 100.0f, "Package 1");
        //given
        member2 = new Member("email2", "name2", "address2", "F"
                , 1.4f, 100.0f, "Package 1");

        //given
        member3 = new Member("email3", "name3", "address3", "M"
                , 1.5f, 100.0f, "Package 1");

        //given
        //        trainer1 = new Trainer("emailt1", "namet1", "address3", "M","s1");
        //given
//        trainer2 = new Trainer("emailt2", "namet2", "address3", "M","s1");
        //given
//        trainer3 = new Trainer("emailt3", "namet3", "address3", "M","s1");
    }

    //given
    @Nested
    //given
    @DisplayName("isValidMemberIndex method test")
        //given
    class MemberIndex {

        //given
        @Nested
        //given
        @DisplayName("Given their are members in the Gym")
            //given
        class Valid {

            //given
            @BeforeEach
            //given
            public void setUp() {
                //given
                gymAPI.addMember(member1);
                //given
                gymAPI.addMember(member2);
                //given
                gymAPI.addMember(member3);
            }

            //given
            @DisplayName("When the index passed is at the lower boundry")
            //given
            @Test
            //given
            public void lower() {
                //given
                boolean result = gymAPI.isValidMemberIndex(0);
                //given
                assertTrue(result, "0 index is good");
            }

            //given
            @DisplayName("When the index passed is at the upper boundry")
            //given
            @Test
            //given
            public void upper() {
                //given
                boolean result = gymAPI.isValidMemberIndex(2);
                //given
                assertTrue(result, "2 index is good");
            }
            //given
            @DisplayName("When the index passed is out of bounds")
            //given
            @Test
            //given
            public void out() {
                //given
                boolean result = gymAPI.isValidMemberIndex(3);
                //given
                assertFalse(result, "0 index is invalid");
            }
        }

        //given
        @Nested
        //given
        @DisplayName("Given their are no members in the Gym")
            //given
        class Empty {
            //given
            @DisplayName("When the index passed is zero")
            //given
            @Test
            //given
            public void lower() {
                //given
                boolean result = gymAPI.isValidMemberIndex(0);
                //given
                assertFalse(result, "0 index is invalid");
            }
        }
    }

    //given
    @Nested
    //given
    @DisplayName("numberOfMember method test")
        //given
    class NumMember {

        //given
        @Nested
        //given
        @DisplayName("Given their are members in the Gym")
        class Some {

            @BeforeEach
            public void setUp() {
                gymAPI.addMember(member1);
                gymAPI.addMember(member2);
                gymAPI.addMember(member3);
            }

            @DisplayName("Should return the correct number")
            @Test
            public void correct() {
                int result = gymAPI.numberOfMembers();
                assertEquals(3, result, "Incorrect no. members");
            }
        }

        @Nested
        @DisplayName("Given their are no members in the Gym")
        class None {

            @DisplayName("Should return zeror")
            @Test
            public void correct() {
                int result = gymAPI.numberOfMembers();
                assertEquals(0, result, "Incorrect no. members");
            }
        }
    }

    @Nested
    @DisplayName("numberOfTrainers method test")
    class NumTraimers {

        @Nested
        @DisplayName("Given their are trainers in the Gym")
        class Some {

            @BeforeEach
            public void setUp() {
                gymAPI.addTrainer(trainer1);
                gymAPI.addTrainer(trainer2);
                gymAPI.addTrainer(trainer3);
            }

            @DisplayName("Should return the correct number")
            @Test
            public void correct() {
                int result = gymAPI.numberOfTrainerss();
                assertEquals(3, result, "Incorrect no. trainers");
            }
        }

        @Nested
        @DisplayName("Given their are no trainers in the Gym")
        class None {

            @DisplayName("Should return zeror")
            @Test
            public void correct() {
                int result = gymAPI.numberOfTrainerss();
                assertEquals(0, result, "Incorrect no. trainers");
            }
        }
    }

    @Nested
    @DisplayName("isValidTrainerIndex method test")
    class TrainerIndex {

        @Nested
        @DisplayName("Given their are trainers in the Gym")
        class Valid {

            @BeforeEach
            public void setUp() {
                gymAPI.addTrainer(trainer1);
                gymAPI.addTrainer(trainer2);
                gymAPI.addTrainer(trainer3);
            }

            @DisplayName("When the index passed is at the lower boundry")
            @Test
            public void lower() {
                boolean result = gymAPI.isValidTrainerIndex(0);
                assertTrue(result, "0 index is good");
            }

            @DisplayName("When the index passed is at the upper boundry")
            @Test
            public void upper() {
                boolean result = gymAPI.isValidTrainerIndex(2);
                assertTrue(result, "2 index is good");
            }
            @DisplayName("When the index passed is out of bounds")
            @Test
            public void out() {
                boolean result = gymAPI.isValidTrainerIndex(3);
                assertFalse(result, "Index is invalid");
            }
        }

        @Nested
        @DisplayName("Given their are no trainers in the Gym")
        class Empty {

            @DisplayName("When the index passed is zero")
            @Test
            public void lower() {
                boolean result = gymAPI.isValidTrainerIndex(0);
                assertFalse(result, "0 index is invalid");
            }
        }
    }
}