import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;

import java.util.HashMap;
import java.util.SortedSet;

import static org.junit.jupiter.api.Assertions.*;

@DisplayName("Tests for Member class")
public class MemberTest {
    Member member1;
    Assessment assessment1;
    Assessment assessment2;
    Assessment assessment3;

    //    Due to the Member class being abstract we need to use a mock member.
    private class MockMember extends Member {

        public MockMember() {
        }

        public MockMember(String email, String name, String address,
                          String gender, float height, float startWeight, String chosenPackage) {

            super(email, name, address, gender, height, startWeight, chosenPackage);
            HashMap<String, Assessment> assessmentCollection = new HashMap<String, Assessment>();

        }

        public void chosenPackage(String chosenPackage) {

        }
    }

    @BeforeEach
    public void setUp() {
        member1 = new MockMember("email1", "name1", "address1", "F",
                1.5f, 100.0f, "Package 1");
    }

    @Nested
    @DisplayName("setStartingWeight method test")
    class Weight {

        @DisplayName("When the value is inside the lower bound")
        @Test
        public void lower() {
            member1.setStartWeight(35.0f);
            assertEquals(35.0f,
                    member1.getStartWeight(),
                    "35.0 should be allowed");
        }

        @DisplayName("When the value is inside the upper bound")
        @Test
        public void upper() {
            member1.setStartWeight(250.0f);
            assertEquals(250.0f,
                    member1.getStartWeight(),
                    "3.0 should be allowed");
        }

        @DisplayName("When the value is outside the boundary")
        @Test
        public void outside() {
            member1.setStartWeight(250.1f);
            assertNotEquals(250.1f,
                    member1.getStartWeight(),
                    "250.1 should not be allowed");
            member1.setStartWeight(34.9f);
            assertNotEquals(34.9f,
                    member1.getStartWeight(),
                    "34.9 should not be allowed");

        }
    }

    @Nested
    @DisplayName("setHeight method test")
    class Height {

        @DisplayName("When the value is inside the lower bound")
        @Test
        public void lower() {
            member1.setHeight(1.0f);
            assertEquals(1.0f,
                    member1.getHeight(),
                    "1.0 should be allowed");
        }

        @DisplayName("When the value is inside the upper bound")
        @Test
        public void upper() {
            member1.setHeight(3.0f);
            assertEquals(3.0f,
                    member1.getHeight(),
                    "3.0 should be allowed");
        }

        @DisplayName("When the value is outside the boundary")
        @Test
        public void outside() {
            member1.setHeight(3.1f);
            assertNotEquals(3.1f,
                    member1.getHeight(),
                    "3.1 should not be allowed");
            member1.setHeight(0.9f);
            assertNotEquals(0.9f,
                    member1.getHeight(),
                    "3.1 should not be allowed");

        }
    }

    @Nested
    @DisplayName("setName method test")
    class Name {

        @DisplayName("When the value is too long")
        @Test
        public void toolong() {
            member1.setName(
                    "1234567890123456789012345678901");
            assertEquals("123456789012345678901234567890",
                    member1.getName(),
                    "31 characters is not allowed");
        }
    }

    @Nested
    @DisplayName("setGender method test")
    class Gender {

        @DisplayName("When the value is M or F")
        @Test
        public void orMF() {
            member1.setGender("M");
            assertEquals("M",
                    member1.getGender(),
                    "M is allowed");
            member1.setGender("F");
            assertEquals("F",
                    member1.getGender(),
                    "F is allowed");

        }

        @DisplayName("When the value is neither M nor F")
        @Test
        public void notMF() {
            member1.setGender("P");
            assertEquals("Unspecified",
                    member1.getGender(), "Should reset to Unspecifie");
        }
    }

    @Nested
    @DisplayName("Constructor tests")
    class Constructir {

        @Nested
        @DisplayName("The height field")
        class Height {
            @DisplayName("When the value is outside the boundary")
            @Test
            public void heightOutside() {
                member1 = new MockMember("email1", "name1", "address1", "F",
                        0.9f, 100.0f, "Package 1");
                assertNotEquals(0.9f,
                        member1.getHeight(),
                        "0.9 should not be allowed");
                member1 = new MockMember("email1", "name1", "address1", "F",
                        3.1f, 100.0f, "Package 1");
                assertNotEquals(3.1f,
                        member1.getHeight(),
                        "3.1 should not be allowed");

            }

            @DisplayName("When the value is inside the boundary")
            @Test
            public void heightInside() {
                member1 = new MockMember("email1", "name1", "address1", "F",
                        1.0f, 100.0f, "Package 1");
                assertEquals(1.0f,
                        member1.getHeight(),
                        "1.0 is allowed");
                member1 = new MockMember("email1", "name1", "address1", "F",
                        3.0f, 100.0f, "Package 1");
                assertEquals(3.0f,
                        member1.getHeight(),
                        "3.0 is allowed");
            }
        }

        @Nested
        @DisplayName("The starting weight field")
        class Weight {
            @DisplayName("When the value is outside the boundary")
            @Test
            public void weightOutside() {
                member1 = new MockMember("email1", "name1", "address1", "F",
                        2.0f, 34.9f, "Package 1");
                assertNotEquals(34.9f,
                        member1.getStartWeight(),
                        "34.9 should not be allowed");
                member1 = new MockMember("email1", "name1", "address1", "F",
                        1.1f, 250.1f, "Package 1");
                assertNotEquals(250.1f,
                        member1.getStartWeight(),
                        "250.1 should not be allowed");
            }

            @DisplayName("When the value is inside the boundary")
            @Test
            public void weightInside() {
                member1 = new MockMember("email1", "name1", "address1", "F",
                        1.0f, 35.0f, "Package 1");
                assertEquals(35.0f,
                        member1.getStartWeight(),
                        "35.0 is allowed");
                member1 = new MockMember("email1", "name1", "address1", "F",
                        3.0f, 250.0f, "Package 1");
                assertEquals(250.0f,
                        member1.getStartWeight(),
                        "250.0 is allowed");
            }
        }

        @Nested
        @DisplayName("The name field")
        class Name {
            @DisplayName("When the value is too long")
            @Test
            public void weightOutside() {
                member1 = new MockMember("email1",
                        "1234567890123456789012345678901",
                        "address1", "F",
                        2.0f, 34.9f, "Package 1");
                assertEquals("123456789012345678901234567890",
                        member1.getName(),
                        "31 characters is not allowed");

            }
        }

        @Nested
        @DisplayName("The gender field")
        class Gender {
            @DisplayName("When the value is F or M")
            @Test
            public void orFM() {
                member1 = new MockMember("email1", "name1", "address1", "F",
                        2.0f, 34.9f, "Package 1");
                assertEquals("F",
                        member1.getGender(),
                        "F allowed");
                member1 = new MockMember("email1", "name1", "address1", "M",
                        1.1f, 250.1f, "Package 1");
                assertEquals("M",
                        member1.getGender(),
                        "M is allowed");
            }

            @DisplayName("When the value is neither F nor M")
            @Test
            public void notFM() {
                member1 = new MockMember("email1", "name1", "address1", "P",
                        2.0f, 34.9f, "Package 1");
                assertEquals("Unspecified",
                        member1.getGender(),
                        "Reset to Unspecigied");
            }
        }
    }

    @Nested
    @DisplayName("sortedAssessmentDates method tests")
    class SortedDates {


        @Nested
        @DisplayName("Given their are some assessments for the member")
        class Some {
            @BeforeEach
            public void setUp() {
                member1.getAssessments().put("18/01/01", assessment1);
                member1.getAssessments().put("18/01/02", assessment2);
                member1.getAssessments().put("18/01/03", assessment3);
            }

            @DisplayName("Should return a sorted date set")
            @Test
            public void sorted() {
                SortedSet<String> result = member1.sortedAssessmentDates();
                assertTrue(result.last() == "18/01/03", "Last date not correct");
                assertTrue(result.first() == "18/01/01", "Last date not correct");
            }
        }
        @Nested
        @DisplayName("Given their are no assessments for the member")
        class None {

            @DisplayName("Should return an empty set")
            @Test
            public void empty() {
                SortedSet<String> result = member1.sortedAssessmentDates();
                assertTrue(result.size() == 0, "Set not empty");
            }
        }
    }

    @Nested
    @DisplayName("latestAssessment method tests")
    class LatestAssessment {

        @Nested
        @DisplayName("Given their are some assessments for the member")
        class Some {
            @BeforeEach
            public void setUp() {
                assessment3 = new Assessment(0.0f,0.0f,0.0f,"comment3");
                member1.getAssessments().put("18/01/01", assessment1);
                member1.getAssessments().put("18/01/02", assessment2);
                member1.getAssessments().put("18/01/03", assessment3);
            }

            @DisplayName("Should return the latest one")
            @Test
            public void latest() {
                Assessment result = member1.latestAssessment();
                assertTrue(result.getComment() == "comment3", "Incorrect assessment returned");
            }
        }
        @Nested
        @DisplayName("Given their are no assessments for the member")
        class None {

            @DisplayName("Should return null")
            @Test
            public void empty() {
                Assessment result = member1.latestAssessment();
                assertNull (result, "Did not return null");
            }
        }
    }
}



