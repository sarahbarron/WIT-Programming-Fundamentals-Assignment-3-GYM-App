import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.CsvSource;

import static org.junit.jupiter.api.Assertions.*;

@DisplayName("Tests for GymUtility class")
public class GymUtilityTest {
    Member member1;
    Assessment assessment1;

    @BeforeEach
    public void setUp() {
        member1 = new Member("email1", "name1", "address1", "F",
                1.5f, 100.0f, "Package 1");
        assessment1 = new Assessment(12.0f, 1.0f, 1.0f, "comment");
    }

    @Nested
    @DisplayName("calculcatBMI method test")
    class BMICalc {

        @DisplayName("Should return the correct result")
        @Test
        public void valid() {
            double result = GymUtility.calculateBMI(member1, assessment1);
            assertEquals(5.33, result, 0.01, "Incorrect no. members");
        }
    }

    @Nested
    @DisplayName("determineBMICategory method test")
    class BMICategory {

        @DisplayName("Normal cases")
        @ParameterizedTest(
                name = "When BMI value is {0}; category should be {1} ")
        @CsvSource({
                "14, SEVERELY UNDERWEIGHT",
                "17, UNDERWEIGHT",
                "20, NORMAL",
                "28, OVERWEIGHT",
                "32, MODERATELY OBESE",
                "37, SEVERELY OBESE"
        })
        public void normal(float v, String c) {
            String result = GymUtility.determineBMICategory(v);
            assertEquals(c, result, "Incorrect no. members");
        }

        @DisplayName("Boundary cases")
        @ParameterizedTest(
                name = "When BMI value is {0}; category should be {1} ")
        @CsvSource({
                "16, UNDERWEIGHT",
                "18.5, NORMAL",
                "25, OVERWEIGHT",
                "30, MODERATELY OBESE",
                "35, SEVERELY OBESE"
        })
        public void boundary(float v, String c) {
            String result = GymUtility.determineBMICategory(v);
            assertEquals(c, result, "Incorrect no. members");
        }
    }

    @Nested
    @DisplayName("isIdealBodyWeight method test")
    class IsIdealWeight {

        @DisplayName("Cases when weight is ideal")
        @ParameterizedTest(
                name = "When gender is {0}, height is {1}m and weight is {2}kg ")
        @CsvSource({
                "M, 2.0, 93.2",
                "F, 2.0, 88.5",
                "Unspecified, 2.0, 88.5"
        })
        public void normal(String g, float height, float weight) {
            member1.setGender(g);
            member1.setHeight(height);
            assessment1.setWeight(weight);
            boolean result = GymUtility.isIdealBodyWeight(member1, assessment1);
            assertTrue(result, "Is an ideal weight");
        }

        @DisplayName("Cases when weight is not ideal")
        @ParameterizedTest(
                name = "When gender is {0}, height is {1}m and weight is {2}kg ")
        @CsvSource({
                "M, 2.0, 100.2",
                "F, 2.0, 78.5",
                "Unspecified, 2.0, 100.5"
        })
        public void abnormal(String g, float height, float weight) {
            member1.setGender(g);
            member1.setHeight(height);
            assessment1.setWeight(weight);
            boolean result = GymUtility.isIdealBodyWeight(member1, assessment1);
            assertFalse(result, "Is not an ideal weight");
        }

        @Nested
        @DisplayName("Given person's height is on the boundary")
        class BoundryHeight {

            @DisplayName("For cases where weight is ideal")
            @ParameterizedTest(
                    name = "When gender is {0}, height is {1}m and weight is {2}kg ")
            @CsvSource({
                    "M, 1.524, 50.1",
                    "F, 1.524, 45.5",
                    "Unspecified, 1.524, 45.5"
            })
            public void boundrygood(String g, float height, float weight) {
                member1.setGender(g);
                member1.setHeight(height);
                assessment1.setWeight(weight);
                boolean result = GymUtility.isIdealBodyWeight(member1, assessment1);
                assertTrue(result, "Is an ideal weight");
            }

            @DisplayName("For cases where weight is not ideal")
            @ParameterizedTest(
                    name = "When gender is {0}, height is {1}m and weight is {2}kg ")
            @CsvSource({
                    "M, 1.524, 60.1",
                    "F, 1.524, 55.5",
                    "Unspecified, 1.524, 55.5"
            })
            public void boundryBad(String g, float height, float weight) {
                member1.setGender(g);
                member1.setHeight(height);
                assessment1.setWeight(weight);
                boolean result = GymUtility.isIdealBodyWeight(member1, assessment1);
                assertFalse(result, "Is not an ideal weight");
            }
        }
    }
}




