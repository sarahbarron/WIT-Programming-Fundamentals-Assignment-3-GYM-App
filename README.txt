	
	Gym Application
	Name : Sarah Barron
	Student Number : 20013679

------------------------------------------------------------------------------------------------------------------------
	Which level unit tests succeed completely:
------------------------------------------------------------------------------------------------------------------------

	All the tests for the outstanding level run successfully

------------------------------------------------------------------------------------------------------------------------
	Which level unit tests succeed partially:
------------------------------------------------------------------------------------------------------------------------

	All the tests for the outstanding level run successfully

------------------------------------------------------------------------------------------------------------------------
	Self reflection - Grading Spectrum Level:
------------------------------------------------------------------------------------------------------------------------

	Having reviewed my work I feel my work falls in the excellent category. I rate myself this as:

	- Data Model: 	I have implemented the Person, Member, Student Member, Premium Member, Trainer, Assessment,
			InputReader, Responder and SupportSystem classes
			
			Member assessments query support 
			Assessment queries can be used to return data that can in turn be used to:
			- Calculate BMI rates
			- Calculate the BMI category
			- Check if a member is of an ideal weight
			- List members of an ideal weight
			- Convert details to imperial and metric values
			- List members in certain BMI categories. 

	- API: 		I have a fully featured API with methods for the following:
			- Add a member (GymAPI class - addMember - line 38)
			- Add a trainer (GymAPI class - addTrainer - line 43)
			- Returning the number of members (GymAPI class - numberOfMembers - line 48)
			- Returning the number of trainers (GymAPI class - numberOfTrainers - line 53) 
			- Search for a member by email (GymAPI class - searchMembersByEmail - line 74)
			- Search for a member by name (GymAPI class - searchMembersByName - line 88)
			- Search for a trainer by email (GymAPI class - searchTrainersByEmail - line 110) 
			- Search for a trainer by name (GymAPI class - searchTrainersByName - line 124)
			- Return a list of members (GymAPI class - listMembers - line 145)
			- List member by a specific BMI category (GymAPI class - listMembersBySpecificBMICategory - line 171)
			- Return a list of members with an ideal weight (GymAPI class - listMembersWithIdealWeight() line 153)
			- Return a list of all members with their weight and height details imperial and metric values 
		  	  (GymAPI - listMemberDetailsImperialAndMetric - line 233)
			
		
			Fully featured Utility class with methods for the following:
			- To calculate a members BMI (GymUtility - calculateBMI - line 13)
			- To determine a members BMI category (GymUtility - determineBMICategory - line 31)
			- To determine if member is of ideal body weight or not (GymUtility - isIdealBodyWeight - line 50)

	- Menu:		A Person can:
			- Register
			- Login
	
			A Member can:
			- View their Profile
			- Update their Profile
			- Search for a trainer by email
			- Search for a trainer by name
			- Get live support from the support system

			A member can view a progress sub menu where they can view:
			- Their weight progress
			- Their waist progress
			- Their thigh progress
			- All comments on their assessments
			- The last assessment comment

			A Trainer can:
			- Add a new member
			- List all members
			- Search for a member by email
			- Search for a member by name
			- List all members with an ideal weight
			- List members in a certain BMI category
			- List member details imperially and metrically

			A Trainer can view an assessment sub menu where they can	
			- Add an assessment to a members account
			- Update a comment on an assessment 	
	
	- Input validation examples:
			- The date inputted for a members assessment is validated by checking user input date matches the pattern
			  yy/mm/dd and checks if the date is a valid date (MenuController - isValidDate method - line 1029)
			- Input for height and weight is checked against maximum and minimum values, if the input falls outside of 
			  the maximum and minimum values the user is asked to input a valid value
            - Input for login or register is validated - If the member enters anything other than l for login,
			  r for register or x for exit. The user told the input is incorrect and are prompted to enter a valid input.
			- Input for asking a person if they are a member or trainer - if the user inputs anything other than m for
			  member, t for trainer or x for exit. The user is told the input is incorrect and are prompted to enter a 
			  valid input.
		    - For login the user is prompted to enter their email address, the email address is checked against the
		      the stored email addresses for members or trainers.
		    - For register the user is prompted to enter their email address. This email address is checked against the
		      stored email addresses for users that are already registered. If the email address is already
		      stored, the user is told that a person with this email address is already registered, and the user is
		      asked if they want to login or register.
		    - The users name can only be a a maximum of 30 characters.
		    - The users gender can only be M or F or Unspecified

	- XML persistence:
	        - Data can be loaded from an xml file ( GymAPI - load method - line 276)
			- Data can be saved to an xml file (GymAPI - store method - line 265)

------------------------------------------------------------------------------------------------------------------------
	A statement of how much of the application specification you implemented.
------------------------------------------------------------------------------------------------------------------------

	I feel I have implemented all of the specification.

	I have a data model that consists of a Premium Member, Student Member, Trainer and Assessment. I have implemented
	all the required rules, field validation rules and class rules
	I have a Gym API which operates between the model classes and the menu driver class. It stores an ArrayList of
	Member and an ArrayList or Trainers. All associated methods are included and working.
	I have a Utility class that has static methods to calculate BMI, determine BMI category and indicate if a member is
	of ideal weight.
	I have a MenuController class that uses the console I/O to interact with user. I create an instance of the GymApi
	class and allow the user to navigate the systems features through a series of menus. I implemented the required
	menu system.
	All tests in the Unit tests are passing at outstanding level.

------------------------------------------------------------------------------------------------------------------------
	Any extra features you wish to bring to the assessors attention, i.e. extra functionality, Java syntax not covered
	in the lectures, non-standard Libraries used
------------------------------------------------------------------------------------------------------------------------

	I implemented a member support system (SystemSupport class, InputReader class and Responder class) 
	This allows a member to input a question. The question or sentence is split into words and each word is checked to
	see if it is a keyword. If it is a keyword the responder sends back the corresponding response, otherwise a random
	default response is returned to the user.
	Keywords are: gained, bmi, ideal, assessment, profile, unmotivated, bad, 1, 2, 3, wit, packages and next. 

	I used the Pattern class (MainController - line 1031)
	I used this class to check the user input date for an assessment matches the pattern yy/mm/dd.

	I used the Java simpleDateFormat class (MainController - line 1034)
	I used this class to convert the string input for the assessment date to a Date.

	I use ParseException (MainController - line 1039)
	This checks for an parse exception - if the date is not a valid date this exception will be thrown

	I used the TreeSet class (Member - line 120)
	I used this to sort the assessment dates in date order.
	

------------------------------------------------------------------------------------------------------------------------
	Known bugs/problems :
------------------------------------------------------------------------------------------------------------------------

	No known bugs or problems 

------------------------------------------------------------------------------------------------------------------------
	Any sources referred to during the development of the assignment (no need to reference lecture/lab materials):
------------------------------------------------------------------------------------------------------------------------

	I looked at the following article which helped me with using the Pattern class for checking the input pattern of the 
	Assessment date
	https://www.baeldung.com/java-date-regular-expressions

	I looked at the following article/code which helped me with using the SimpleDateFormat class and ParseException, 
	for converting a string to a Date and checking if the date is a valid date.
	http://www.java2s.com/Tutorial/Java/0120__Development/CheckifaStringisavaliddate.htm
