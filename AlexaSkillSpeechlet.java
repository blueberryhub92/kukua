/**
    Copyright 2014-2015 Amazon.com, Inc. or its affiliates. All Rights Reserved.

    Licensed under the Apache License, Version 2.0 (the "License"). You may not use this file except in compliance with the License. A copy of the License is located at

        http://aws.amazon.com/apache2.0/

    or in the "license" file accompanying this file. This file is distributed on an "AS IS" BASIS, WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied. See the License for the specific language governing permissions and limitations under the License.
 */
package com.amazon.customskill;

import java.sql.Connection;

import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.Random;





import java.util.Arrays;
import java.util.List;
import java.util.Random;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
import java.util.stream.IntStream;

import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.amazon.customskill.AlexaSkillSpeechlet.RecognitionState;
import com.amazon.customskill.AlexaSkillSpeechlet.UserIntent;
import com.amazon.speech.json.SpeechletRequestEnvelope;
import com.amazon.speech.slu.Intent;
import com.amazon.speech.speechlet.IntentRequest;
import com.amazon.speech.speechlet.LaunchRequest;
import com.amazon.speech.speechlet.SessionEndedRequest;
import com.amazon.speech.speechlet.SessionStartedRequest;
import com.amazon.speech.speechlet.SpeechletResponse;
import com.amazon.speech.speechlet.SpeechletV2;
import com.amazon.speech.ui.PlainTextOutputSpeech;
import com.amazon.speech.ui.Reprompt;
import com.amazon.speech.ui.SsmlOutputSpeech;

/*import nlp.dkpro.backend.LinguisticPreprocessor;
import nlp.dkpro.backend.NlpSingleton;*/




/*
 * This class is the actual skill. Here you receive the input and have to produce the speech output. 
 */
public class AlexaSkillSpeechlet
implements SpeechletV2
{
	public static Connection connect() {
	    Connection con = null; 
	    try {
	      Class.forName("org.sqlite.JDBC");
	      con = DriverManager.getConnection("jdbc:sqlite:/Users/raphaelstedler/Desktop/Praxisprojekt-master/de.unidue.ltl.ourWWM/Vokabeln.db"); // connecting to our database
	      logger.info("Connected!");
	    } catch (ClassNotFoundException | SQLException e ) {
	      logger.info(e+"");
	    }
	    return con; 
	  }
	
	static Logger logger = LoggerFactory.getLogger(AlexaSkillSpeechlet.class);

	public static String userRequest;

	private static int sum;
	private static int sum2;
	private static int sum3;
	private static int sum4;
	private static int sum5;
	private static int sum6;
	private static int sum7;
	private static String question = "";
	private static String antonym = "";
	private static String Answer = "";
	private static String correctAnswer = "";
	private static String correctAnswer2 = "";
	public static enum RecognitionState {YesNoQuizLevelEnd, YesNoQuizLevelOne, YesNoQuizLevelTwo, YesNoQuizLevelThree, YesNoVokabelnEasy, YesNoVokabelnBasics, YesNoVokabelnHard, AnswerQuizLevelOne, AnswerQuizLevelTwo, AnswerQuizLevelThree, AnswerVokabelnEasy, AnswerVokabelnMedium, AnswerVokabelnHard, Answer, AnswerTwo, AnswerThree, AnswerFour, AnswerFive, AnswerSix, AnswerSeven, YesNo, YesNoTwo, YesNoLevel, YesNoLevelTwo, OneTwo, VokabelQuiz, Vokabel, WhichPlayer, WhichPlayerThree, WhichPlayerFour, AgainOrMenu, resumequizzen, SingleQuiz, YesNoQuiz, YesNoVokabeln, AnswerVokabeln, AnswerQuiz};
	private RecognitionState recState;
	public static enum UserIntent {Answer, again, vocab, levelone, leveltwo, difficulty, onne, twwo, menu, playerone, playertwo, vocabulary, quiz, resume, yess, no, quit, easy, basics, expressions, nextlevel, Error, Quiz};
	UserIntent ourUserIntent;

	static String welcomeMsg = "Hello and welcome at Quizzitch. How many players want to play, one or two players?";
	static String singleMsg = "You're in single mode. Do you want to train vocabulary first or starting a quiz?";
	static String multiMsg = "You're in two player mode. Please clarify who wants to be player one and who wants to be player two. If you think you know the correct answer, say you're player number. You will get points if your answer is correct. Let's begin!";	
	static String ThemeMsg = "You can choose between different themes. Tell me the area you want to learn vocabulary in or choose all vocabulary.";
	static String singleQuizMsg = "Welcome to the single quiz mode. Let's begin!";
	static String antonymMsg = "What's the antonym of ";
	static String wrongMsg = "That's wrong. The correct answer would be";
	static String wrongVocMsg = "That's wrong. The correct answer would be";
	static String dontknowMsg = "What a pity. The correct answer would be";
	static String correctMsg = "That´s correct.";
	static String correctAnswerMsg = "The correct answer would be ";
	static String continueMsg = "Do you want to resume playing?";
	static String congratsMsg = "Congratulations! You've won one {replacement} points.";
	static String goodbyeMsg = "I hope to hear from you soon, good bye!";
	static String sumMsg = "You've {replacement} points. ";
	static String sumTwoMsg = "The score is {replacement3} ";
	static String sumThreeMsg = "to {replacement5}.";
	static String errorYesNoMsg = "Sorry, I did not unterstand that. Please say resume or quit.";
	static String errorAgainOrMenuMsg = "Sorry I did not unterstand that. Please say menu, again or quit.";
	static String errorAnswerMsg = "Sorry I did not unterstand that. Please mention your answer again.";
	static String errorOneTwoMsg = "Unfortunately I did not unterstand that. Please say one or two.";
	static String errorVokabelQuizMsg = "Unfortunately I did not understand that. Say vocabulary or quiz.";
	static String errorVokabelMsg = "Do you want to train your vocabulary in a certain area? If so, choose one or continue with all areas.";
	static String VokabelLeicht = "Welcome to the easy vocab trainer";
	static String VokabelMittel = "Welcome to the medium vocab trainer";
	static String VokabelSchwer = "Welcome to the hard vocab trainer";
	static String errorSpielereinszweiMsg = "Which player knows the correct answer?";
	static String SpielerEinsMsg = "Player one was faster. What's your answer?";
	static String SpielerEinsKurzMsg = "Player one?";
	static String SpielerZweiMsg = "Player two was faster. What's your answer?";
	static String SpielerZweiKurzMsg = "Player two?";
	static String continueLevelMsg = "Level two is up. Do you want to contiunue?";
	static String continueLevelTwoMsg = "Level three is up. Do you want to contiunue?";
	static String continueEinzelQuizLevelTwoMsg = "Congratulations! You've accomplished Level one. Do you want to jump to the second level?";
	static String continueEinzelQuizLevelThreeMsg = "Congratulations! You've accomplished Level two. Do you want to jump to the third level?";
	static String continueEinzelQuizEndMsg = "Congratulations! You made it all the way up! You've accomplished all levels! Do you want to resume the quiz, change level or go back to the menu?";
	static String playerOneWins = "Player one has won this round.";
	static String playerTwoWins = "Player two has won this round.";
	static String playerOneWinsGame = "Player one has won the game.";
	static String playerTwoWinsGame = "Player two has won the game.";
	static String againOrMenuMsg = "Do you want to play again, go back to the menu or quit?";
	static String resumequizzenMsg = "Do you want to play a quiz instead or quit the app??";
	static String errorresumequizzen = "Sorry I did not understand that. Please say quiz or quit the app.";
	static String resumeVokabelnMsg = "Do you want to the some vocabulary instead or quit the app?";
	

	private String buildString(String msg, String replacement1, String replacement2) {
		return msg.replace("{replacement}", replacement1).replace("{replacement2}", replacement2);
	}
	
	private String buildString2(String msg, String replacement3, String replacement4) {
		return msg.replace("{replacement3}", replacement3).replace("{replacement4}", replacement4);
	}
	
	private String buildString3(String msg, String replacement5, String replacement6) {
		return msg.replace("{replacement5}", replacement5).replace("{replacement6}", replacement6);
	}


	@Override
	public void onSessionStarted(SpeechletRequestEnvelope<SessionStartedRequest> requestEnvelope)
	{
		logger.info("Alexa session begins");
		sum = 0;
		sum2 = 0;
		sum3 = 0;
		sum4 = 0;
		sum5 = 0;
		sum6 = 0;
		sum7 = 0;
		recState = RecognitionState.OneTwo;
	}

	@Override
	public SpeechletResponse onLaunch(SpeechletRequestEnvelope<LaunchRequest> requestEnvelope)
	{

		return responseWithFlavour(welcomeMsg, 5);
		
	}
	
	
	
	private String selectQuestion() {
		
		Answer = "correct answer";
		
		Connection con = AlexaSkillSpeechlet.connect(); 
		  PreparedStatement ps = null; 
		  ResultSet rs = null; 
		  
		  
		  try {
			 logger.info("Try-Block");
			 
			//String sql = "SELECT * FROM Vokabelliste WHERE Thema LIKE 'Grundlagen' ORDER BY RANDOM() LIMIT 1";
		    String sql = "SELECT * FROM Vokabelliste ORDER BY RANDOM() LIMIT 1";
		    ps = con.prepareStatement(sql); 
		    rs = ps.executeQuery();
		    
		    while(rs.next()) {
		      /*int number = rs.getInt("number");*/
		     question = rs.getString("de"); 
		     correctAnswer = rs.getString("en");
		     String Thema = rs.getString("Thema");
		     return question+correctAnswer;
		       
		    }
		      	  }
		  	   catch(SQLException e) {
		    //System.out.println(e.toString());
		  } 
		  return null;
		  }
	
private String selectQuestion2() {
		
		Answer = "correct answer";
		
		Connection con = AlexaSkillSpeechlet.connect(); 
		  PreparedStatement ps = null; 
		  ResultSet rs = null; 
		  
		  
		  try {
			 logger.info("Try-Block");
			 
			String sql = "SELECT * FROM Vokabelliste WHERE Thema LIKE 'Grundlagen' ORDER BY RANDOM() LIMIT 1";
		    //String sql = "SELECT * FROM Vokabelliste ORDER BY RANDOM() LIMIT 1";
		    ps = con.prepareStatement(sql); 
		    rs = ps.executeQuery();
		    
		    while(rs.next()) {
		     //int number = rs.getInt("number");
		     question = rs.getString("de"); 
		     correctAnswer = rs.getString("en");
		     String Thema = rs.getString("Thema");
		     return question+correctAnswer;
		       
		    }
		      	  }
		  	   catch(SQLException e) {
		    //System.out.println(e.toString());
		  } 
		  return null;
		  }
		
private String selectQuestion3() {
	
	Answer = "correct answer";
	
	Connection con = AlexaSkillSpeechlet.connect(); 
	  PreparedStatement ps = null; 
	  ResultSet rs = null; 
	  
	  
	  try {
		 logger.info("Try-Block");
		 
		String sql = "SELECT * FROM Vokabelliste WHERE Thema LIKE 'Ausdrücke' ORDER BY RANDOM() LIMIT 1";
	    //String sql = "SELECT * FROM Vokabelliste ORDER BY RANDOM() LIMIT 1";
	    ps = con.prepareStatement(sql); 
	    rs = ps.executeQuery();
	    
	    while(rs.next()) {
	     //int number = rs.getInt("number");
	     question = rs.getString("de"); 
	     correctAnswer = rs.getString("en");
	     String Thema = rs.getString("Thema");
	     return question+correctAnswer;
	       
	    }
	      	  }
	  	   catch(SQLException e) {
	    //System.out.println(e.toString());
	  } 
	  return null;
	  }
	
	private String selectAntonym() {
		
		Connection con = AlexaSkillSpeechlet.connect(); 
		  PreparedStatement ps = null; 
		  ResultSet rs = null; 
		  
		  try {
			 logger.info("Try-Block");
			 String sql2 = "SELECT * FROM gegenteile ORDER BY RANDOM() LIMIT 1";
			    ps = con.prepareStatement(sql2); 
			    rs = ps.executeQuery();
			    while(rs.next()) {
			     antonym = rs.getString("word"); 
			     correctAnswer2 = rs.getString("antonym");
			      return antonym+correctAnswer2;  
			    }
			      }
		     catch(SQLException e) {
		    //System.out.println(e.toString());
		  } 
		 // return question+correctAnswer;
		  return null;
		  }


	@Override
	public SpeechletResponse onIntent(SpeechletRequestEnvelope<IntentRequest> requestEnvelope)
	{
		IntentRequest request = requestEnvelope.getRequest();
		Intent intent = request.getIntent();
		userRequest = intent.getSlot("anything").getValue();
		logger.info("Received following text: [" + userRequest + "]");
		logger.info("recState is [" + recState + "]");
		SpeechletResponse resp = null;
		switch (recState) {
		case OneTwo: resp = evaluateOneTwo(userRequest); break;
		case VokabelQuiz: resp = evaluateVokabelQuiz(userRequest); break;
		case YesNo: resp = evaluateYesNo(userRequest); break;
		case WhichPlayer: resp = evaluateWhichPlayer(userRequest); break;
		case WhichPlayerThree: resp = evaluateWhichPlayerThree(userRequest); break;
		case WhichPlayerFour: resp = evaluateWhichPlayerFour(userRequest); break;
		case Vokabel: resp = evaluateVokabel(userRequest); break;
		case AnswerTwo: resp = evaluateAnswerTwo(userRequest); break;
		case AnswerThree: resp = evaluateAnswerThree(userRequest); break;
		case AnswerFour: resp = evaluateAnswerFour(userRequest); break;
		case AnswerFive: resp = evaluateAnswerFive(userRequest); break;
		case AnswerSix: resp = evaluateAnswerSix(userRequest); break;
		case AnswerSeven: resp = evaluateAnswerSeven(userRequest); break;
		case YesNoTwo: resp = evaluateYesNoTwo(userRequest); break;
		case YesNoLevel: resp = evaluateYesNoLevel(userRequest); break;
		case YesNoLevelTwo: resp = evaluateYesNoLevelTwo(userRequest); break;
		case AgainOrMenu: resp = evaluateAgainOrMenu(userRequest); break;
		case SingleQuiz: resp = evaluateSingleQuiz(userRequest); break;
		case YesNoQuizLevelOne: resp = evaluateYesNoQuizLevelOne(userRequest); break;
		case YesNoQuizLevelTwo: resp = evaluateYesNoQuizLevelTwo(userRequest); break;
		case YesNoQuizLevelThree: resp = evaluateYesNoQuizLevelThree(userRequest); break;
		case YesNoQuizLevelEnd: resp = evaluateYesNoQuizLevelEnd(userRequest); break;
		case YesNoVokabelnEasy: resp = evaluateYesNoVokabelnEasy(userRequest); break;
		case YesNoVokabelnBasics: resp = evaluateYesNoVokabelnBasics(userRequest); break;
		//case YesNoVokabelnHard: resp = evaluateYesNoVokabelnHard(userRequest); break;
		case AnswerVokabelnEasy: resp = evaluateAnswerVokabelnEasy(userRequest); break;
		//case AnswerVokabelnMedium: resp = evaluateAnswerVokabelnMedium(userRequest); break;
		//case AnswerVokabelnHard: resp = evaluateAnswerVokabelnHard(userRequest); break;
		case AnswerQuizLevelOne: resp = evaluateAnswerQuizLevelOne(userRequest); break;
		case AnswerQuizLevelTwo: resp = evaluateAnswerQuizLevelTwo(userRequest); break;
		case AnswerQuizLevelThree: resp = evaluateAnswerQuizLevelThree(userRequest); break;
		default: resp = response("Erkannter Text: " + userRequest);
		}   
		return resp;
	}

	/* Im Vokabelteil: Möchten Sie resume machen? (-> stattdessen Quizzen?) */
	private SpeechletResponse evaluateYesNoVokabelnEasy(String userRequest) {	
		SpeechletResponse res = null;
		
		recognizeUserIntent(userRequest);
		switch (ourUserIntent) {
		case resume: {
			selectQuestion();
			res = responseWithFlavour(question, 6);
			recState = RecognitionState.AnswerVokabelnEasy; break;
		} case yess: {
			selectQuestion();
			res = responseWithFlavour(question, 6);
			recState = RecognitionState.AnswerVokabelnEasy; break;
			
		} case quit: {
			res = responseWithFlavour2(goodbyeMsg, 0); break;
		} case no: {
			res = responseWithFlavour2(goodbyeMsg, 0); break;
			
		} case menu: {
			res = responseWithFlavour(welcomeMsg, 5);
			recState = RecognitionState.OneTwo; break;
		} case difficulty: {
			res = askUserResponse(ThemeMsg);
			recState = RecognitionState.Vokabel; break;
		} default: {
			res = askUserResponse(errorYesNoMsg);
		}
		}
		return res;
	}
	
	private SpeechletResponse evaluateYesNoVokabelnBasics(String userRequest) {	
		SpeechletResponse res = null;
		
		recognizeUserIntent(userRequest);
		switch (ourUserIntent) {
		case resume: {
			selectQuestion2();
			res = responseWithFlavour(question, 6);
			recState = RecognitionState.AnswerVokabelnEasy; break;
		} case yess: {
			selectQuestion2();
			res = responseWithFlavour(question, 6);
			recState = RecognitionState.AnswerVokabelnEasy; break;
			
		} case quit: {
			res = responseWithFlavour2(goodbyeMsg, 0); break;
		} case no: {
			res = responseWithFlavour2(goodbyeMsg, 0); break;
			
		} case menu: {
			res = responseWithFlavour(welcomeMsg, 5);
			recState = RecognitionState.OneTwo; break;
		} case difficulty: {
			res = askUserResponse(ThemeMsg);
			recState = RecognitionState.Vokabel; break;
		} default: {
			res = askUserResponse(errorYesNoMsg);
		}
		}
		return res;
	}

	
	private SpeechletResponse evaluateYesNoQuizLevelOne(String userRequest) {	
		SpeechletResponse res = null;
		recognizeUserIntent(userRequest);
		switch (ourUserIntent) {
		case resume: {
			selectAntonym();
			res = responseWithFlavour(antonymMsg+" "+antonym+"?",0);
			recState = RecognitionState.AnswerQuizLevelOne; break;
		} case yess: {
			selectAntonym();
			res = responseWithFlavour(antonymMsg+" "+antonym+"?",0);
			recState = RecognitionState.AnswerQuizLevelOne; break;
			
		} case quit: {
			res = responseWithFlavour2(goodbyeMsg, 0); break;
		} case no: {
			res = responseWithFlavour2(goodbyeMsg, 0); break;
		} case menu: {
			res = responseWithFlavour(welcomeMsg, 5);
			recState = RecognitionState.OneTwo; break;
			
		} default: {
			res = askUserResponse(errorYesNoMsg);
		}
		}
		return res;
	}
	
	private SpeechletResponse evaluateYesNoQuizLevelTwo(String userRequest) {	
		SpeechletResponse res = null;
		recognizeUserIntent(userRequest);
		switch (ourUserIntent) {
		case resume: {
			selectAntonym();
			res = responseWithFlavour(/*"Here we go:"+" "+*/antonymMsg+" "+antonym+"?",0);
			recState = RecognitionState.AnswerQuizLevelTwo; break;
		} case yess: {
			selectAntonym();
			res = responseWithFlavour(/*"Here we go:"+" "+*/antonymMsg+" "+antonym+"?",0);
			recState = RecognitionState.AnswerQuizLevelTwo; break;
			
		} case quit: {
			res = responseWithFlavour2(goodbyeMsg, 0); break;
		} case no: {
			res = responseWithFlavour2(goodbyeMsg, 0); break;
			
		} case menu: {
			res = responseWithFlavour(welcomeMsg, 5);
			recState = RecognitionState.OneTwo; break;
			
		} default: {
			res = askUserResponse(errorYesNoMsg);
		}
		}
		return res;
	}
	
	private SpeechletResponse evaluateYesNoQuizLevelThree(String userRequest) {	
		SpeechletResponse res = null;
		recognizeUserIntent(userRequest);
		switch (ourUserIntent) {
		case resume: {
			selectAntonym();
			res = responseWithFlavour(/*"Here we go:"+" "+*/antonymMsg+" "+antonym+"?",0);
			recState = RecognitionState.AnswerQuizLevelThree; break;
		} case yess: {
			selectAntonym();
			res = responseWithFlavour(/*"Here we go:"+" "+*/antonymMsg+" "+antonym+"?",0);
			recState = RecognitionState.AnswerQuizLevelThree; break;
			
		} case quit: {
			res = responseWithFlavour2(goodbyeMsg, 0); break;
		} case no: {
			res = responseWithFlavour2(goodbyeMsg, 0); break;
			
		} case menu: {
			res = responseWithFlavour(welcomeMsg, 5);
			recState = RecognitionState.OneTwo; break;
			
		} default: {
			res = askUserResponse(errorYesNoMsg);
		}
		}
		return res;
	}
	
	private SpeechletResponse evaluateYesNoQuizLevelEnd(String userRequest) {	
		SpeechletResponse res = null;
		recognizeUserIntent(userRequest);
		switch (ourUserIntent) {
		case resume: {
			selectAntonym();
			res = responseWithFlavour("Alright let's go on with the Quiz!"+" "+antonymMsg+" "+antonym+"?",0);
			recState = RecognitionState.AnswerQuizLevelThree; break;
		} case yess: {
			selectAntonym();
			res = responseWithFlavour("Alright let's go on with the Quiz!"+" "+antonymMsg+" "+antonym+"?",0);
			recState = RecognitionState.AnswerQuizLevelThree; break;
			
		} case quit: {
			res = responseWithFlavour2(goodbyeMsg, 0); break;
		} case no: {
			res = responseWithFlavour2(goodbyeMsg, 0); break;
		} case levelone: {
			res = responseWithFlavour("Here we go:"+" "+antonymMsg+" "+antonym+"?",0);
			recState = RecognitionState.AnswerQuizLevelOne; break;
		} case leveltwo: {
			res = responseWithFlavour("Here we go:"+" "+antonymMsg+" "+antonym+"?",0);
			recState = RecognitionState.AnswerQuizLevelTwo; break;
			
		} case menu: {
			res = responseWithFlavour(welcomeMsg, 5);
			recState = RecognitionState.OneTwo; break;
			
		} default: {
			res = askUserResponse(errorYesNoMsg);
		}
		}
		return res;
	}
	
	
	/*resume Weiter Vokabeln lernen?*/
	private SpeechletResponse evaluateYesNo(String userRequest) {
		SpeechletResponse res = null;
		recognizeUserIntent(userRequest);
		switch (ourUserIntent) {
		case resume: {
			selectQuestion();
			res = responseWithFlavour(question, 6);
			recState = RecognitionState.Answer; break;
		} case yess: {
			selectQuestion();
			res = responseWithFlavour(question, 6);
			recState = RecognitionState.Answer; break;	
		} case quit: {
			res = responseWithFlavour2(goodbyeMsg, 0); break;
		} case no: {
			res = responseWithFlavour2(goodbyeMsg, 0); break;
		} case menu: {
			res = askUserResponse(welcomeMsg);
			recState = RecognitionState.OneTwo; break;
		} default: {
			res = askUserResponse(errorYesNoMsg);
		}
		}
		return res;
	}
	
	/*resume spielen oder aufhören, Level 1?*/
	private SpeechletResponse evaluateYesNoTwo(String userRequest) {
		SpeechletResponse res = null;
		recognizeUserIntent(userRequest);
		switch (ourUserIntent) {
		case resume: {
			selectQuestion();
			res = responseWithFlavour(question, 6);
			recState = RecognitionState.WhichPlayer; break;
		} case yess: {
			selectQuestion();
			res = responseWithFlavour(question, 6);
			recState = RecognitionState.WhichPlayer; break;
		} case quit: {
			res = responseWithFlavour2(goodbyeMsg, 0); break;
		} case no: {
			res = responseWithFlavour2(goodbyeMsg, 0); break;
		} case menu: {
			res = responseWithFlavour(welcomeMsg, 5);
			recState = RecognitionState.OneTwo; break;
		} default: {
			res = askUserResponse(errorYesNoMsg);
		}
		}
		return res;
	}
	
	/*resume spielen oder aufhören, Level?*/
	private SpeechletResponse evaluateYesNoLevel(String userRequest) {
		SpeechletResponse res = null;
		recognizeUserIntent(userRequest);
		switch (ourUserIntent) {
		case resume: {
			selectQuestion();
			res = responseWithFlavour(question, 6);
			recState = RecognitionState.WhichPlayerThree; break;
		} case yess: {
			selectQuestion();
			res = responseWithFlavour(question, 6);
			recState = RecognitionState.WhichPlayerThree; break;
		} case quit: {
			res = responseWithFlavour2(goodbyeMsg, 0); break;
		} case no: {
			res = responseWithFlavour2(goodbyeMsg, 0); break;
		} case menu: {
			res = responseWithFlavour(welcomeMsg, 5);
			recState = RecognitionState.OneTwo; break;
		} default: {
			res = askUserResponse(errorYesNoMsg);
		}
		}
		return res;
	}
	
	/*resume spielen oder aufhören, Level?*/
	private SpeechletResponse evaluateYesNoLevelTwo(String userRequest) {
		SpeechletResponse res = null;
		recognizeUserIntent(userRequest);
		switch (ourUserIntent) {
		case resume: {
			selectQuestion();
			res = responseWithFlavour(question, 6);
			recState = RecognitionState.WhichPlayerFour; break;
		} case yess: {
			selectQuestion();
			res = responseWithFlavour(question, 6);
			recState = RecognitionState.WhichPlayerFour; break;
		} case quit: {
			res = responseWithFlavour2(goodbyeMsg, 0); break;
		} case no: {
			res = responseWithFlavour2(goodbyeMsg, 0); break;
		} case menu: {
			res = responseWithFlavour(welcomeMsg, 5);
			recState = RecognitionState.OneTwo; break;
		} default: {
			res = askUserResponse(errorYesNoMsg);
		}
		}
		return res;
	}
	
	private SpeechletResponse evaluateAgainOrMenu(String userRequest) {
		SpeechletResponse res = null;
		recognizeUserIntent(userRequest);
		switch (ourUserIntent) {
		case again: {
			sum = 0;
			sum2 = 0;
			sum3 = 0;
			sum4 = 0;
			sum5 = 0;
			sum6 = 0;
			sum7 = 0;
			selectQuestion();
			res = responseWithFlavour(question, 6);
			recState = RecognitionState.WhichPlayer; break;
		} case quit: {
			res = responseWithFlavour2(goodbyeMsg, 0); break;
		} case menu: {
			res = responseWithFlavour(welcomeMsg, 5);
			sum = 0;
			sum2 = 0;
			sum3 = 0;
			sum4 = 0;
			sum5 = 0;
			sum6 = 0;
			sum7 = 0;
			recState = RecognitionState.OneTwo; break;
		} default: {
			res = askUserResponse(errorAgainOrMenuMsg);
		}
		}
		return res;
	}
	
	/*Ein oder zwei Spieler?*/
	private SpeechletResponse evaluateOneTwo(String userRequest) {
		SpeechletResponse res = null;
		recognizeUserIntent(userRequest);
		switch (ourUserIntent) {
		case onne: {
			res = askUserResponse(singleMsg);
			recState = RecognitionState.VokabelQuiz; break;
		} case twwo: {
			selectQuestion();
			res = responseWithFlavour(question, 10);
			recState = RecognitionState.WhichPlayer; break;
		} default: {
			res = askUserResponse(errorOneTwoMsg);
		}
		}
		return res;
	}
	
	/*Vokabeln oder Quiz?*/
	private SpeechletResponse evaluateVokabelQuiz(String userRequest) {
		SpeechletResponse res = null;
		recognizeUserIntent(userRequest);
		switch (ourUserIntent) {
		case vocabulary: {
			res = askUserResponse(ThemeMsg);
			recState = RecognitionState.Vokabel; break;
		} case quiz: {
			selectAntonym();
			res = responseWithFlavour(singleQuizMsg+" "+antonymMsg+" "+antonym+"?",0);
			recState = RecognitionState.AnswerQuizLevelOne; break;
		} case menu: {
			res = responseWithFlavour(welcomeMsg, 5);
			recState = RecognitionState.OneTwo; break;
		} default: {
			res = askUserResponse(errorVokabelQuizMsg);
		}
		}
		return res;
	}

	/*Schwierigkeit einfach, mittel oder schwer?*/
	private SpeechletResponse evaluateVokabel(String userRequest) {
		SpeechletResponse res = null;
		recognizeUserIntent(userRequest);
		switch (ourUserIntent) {
		case easy: {
			selectQuestion();
			res = responseWithFlavour(question, 6);
			recState = RecognitionState.AnswerVokabelnEasy; break;
		} case basics: {
			selectQuestion2();
			res = responseWithFlavour(question, 6);
			recState = RecognitionState.AnswerVokabelnEasy; break;
		} case expressions: {
			selectQuestion3();
			res = responseWithFlavour(question, 6);
			recState = RecognitionState.AnswerVokabelnEasy; break;
		/*} case hard: {
			selectQuestion11();
			res = askUserResponse(question11);
			recState = RecognitionState.AnswerVokabelnHard; break;*/
		} case menu: {
			res = responseWithFlavour(welcomeMsg, 5);
			recState = RecognitionState.OneTwo; break;
		} default: {
			res = askUserResponse(errorVokabelMsg);
		}
		}
		return res;
	}
	
	/* sind sie bereit? */
	private SpeechletResponse evaluateSingleQuiz(String userRequest) {
		SpeechletResponse res = null;
		recognizeUserIntent(userRequest);
		switch (ourUserIntent) {
		
		
		case yess: {
			selectAntonym();
			res = responseWithFlavour("Alright let's go on with the Quiz!"+" "+antonymMsg+" "+antonym+"?",0);
			recState = RecognitionState.AnswerQuiz; break;
		} 
		
		case no: {
			res = responseWithFlavour2(goodbyeMsg, 0); break;
		}
		
		default: {
			res = askUserResponse(errorVokabelMsg);
		}
		}
		return res;
	}
	
	/*Wer weiß die Antwort?*/
	private SpeechletResponse evaluateWhichPlayer(String userRequest) {
		SpeechletResponse res = null;
		recognizeUserIntent(userRequest);
		switch (ourUserIntent) {
		case playerone: {
			res = askUserResponse(SpielerEinsMsg);
			recState = RecognitionState.AnswerTwo; break;
		} case onne: {
			res = askUserResponse(SpielerEinsMsg);
			recState = RecognitionState.AnswerTwo; break;
		} case playertwo: {
			res = askUserResponse(SpielerZweiMsg);
			recState = RecognitionState.AnswerThree; break;
		} case twwo: {
			res = askUserResponse(SpielerZweiMsg);
			recState = RecognitionState.AnswerThree; break;
		} case menu: {
			res = responseWithFlavour(welcomeMsg, 5);
			recState = RecognitionState.OneTwo; break;
		} default: {
			res = askUserResponse(errorSpielereinszweiMsg);
		}
		}
		return res;
	}
	
	/*Wer weiß die Antwort, Level 2?*/
	private SpeechletResponse evaluateWhichPlayerThree(String userRequest) {
		SpeechletResponse res = null;
		recognizeUserIntent(userRequest);
		switch (ourUserIntent) {
		case playerone: {
			res = askUserResponse(SpielerEinsKurzMsg);
			recState = RecognitionState.AnswerFour; break;
		} case onne: {
			res = askUserResponse(SpielerEinsKurzMsg);
			recState = RecognitionState.AnswerFour; break;
		} case playertwo: {
			res = askUserResponse(SpielerZweiKurzMsg);
			recState = RecognitionState.AnswerFive; break;
		} case twwo: {
			res = askUserResponse(SpielerZweiKurzMsg);
			recState = RecognitionState.AnswerFive; break;
		} case menu: {
			res = responseWithFlavour(welcomeMsg, 5);
			recState = RecognitionState.OneTwo; break;
		} default: {
			res = askUserResponse(errorSpielereinszweiMsg);
		}
		}
		return res;
	}
	
	/*Wer weiß die Antwort, Level 3?*/
	private SpeechletResponse evaluateWhichPlayerFour(String userRequest) {
		SpeechletResponse res = null;
		recognizeUserIntent(userRequest);
		switch (ourUserIntent) {
		case playerone: {
			res = askUserResponse(SpielerEinsKurzMsg);
			recState = RecognitionState.AnswerSix; break;
		} case onne: {
			res = askUserResponse(SpielerEinsKurzMsg);
			recState = RecognitionState.AnswerSix; break;
		} case playertwo: {
			res = askUserResponse(SpielerZweiKurzMsg);
			recState = RecognitionState.AnswerSeven; break;
		} case twwo: {
			res = askUserResponse(SpielerZweiKurzMsg);
			recState = RecognitionState.AnswerSeven; break;
		} case menu: {
			res = responseWithFlavour(welcomeMsg, 5);
			recState = RecognitionState.OneTwo; break;
		} default: {
			res = askUserResponse(errorSpielereinszweiMsg);
		}
		}
		return res;
	}
	
	/*in den Vokabeln Easy*/
	private SpeechletResponse evaluateAnswerVokabelnEasy(String userRequest) {
		SpeechletResponse res = null;
		recognizeUserIntent(userRequest);{
				logger.info("User answer ="+ userRequest.toLowerCase()+ "/correct answer="+correctAnswer);
				if (userRequest.toLowerCase().equals(correctAnswer.toLowerCase())) {
					logger.info("User answer recognized as correct.");
						recState = RecognitionState.YesNoVokabelnEasy;
						res = responseWithFlavour(correctMsg+" "+continueMsg,8);
				}else if (userRequest.toLowerCase().equals(Answer)) {
						 logger.info("User doesn´t know the answer.");
							 recState = RecognitionState.YesNoVokabelnEasy;
							 res = askUserResponse(correctAnswerMsg+" "+correctAnswer+". "+continueMsg);
				} else {
					recState = RecognitionState.YesNoVokabelnEasy;
					res = responseWithFlavour(wrongVocMsg+" "+correctAnswer+". "+continueMsg,7);
				}
			}
		return res;
	}

	
	/*im Einzel Quiz Level1*/
	private SpeechletResponse evaluateAnswerQuizLevelOne(String userRequest) {
		SpeechletResponse res = null;
		recognizeUserIntent(userRequest);{
			logger.info("User answer ="+ userRequest.toLowerCase()+ "/correct answer="+correctAnswer2);
			if (userRequest.toLowerCase().equals(correctAnswer2.toLowerCase())) {
				logger.info("User answer recognized as correct.");
					increaseSum();
					if (sum >= 50) {
						recState = RecognitionState.YesNoQuizLevelTwo;
						res = responseWithFlavour(correctMsg+" "+buildString(sumMsg, String.valueOf(sum), " ")+" "+continueEinzelQuizLevelTwoMsg, 9);
					} else {
						recState = RecognitionState.YesNoQuizLevelOne;
						res = responseWithFlavour(correctMsg+" "+buildString(sumMsg, String.valueOf(sum), " ")+" "+continueMsg, 8);
						
					}
		// Case für den Fall, falls der User keine antwort abgeben möchte, sondern nur etwas wie "I don't know":
					
		 /* } else if (userRequest.toLowerCase().equals("dont know")) {
				recState = RecognitionState.YesNoQuizLevelOne;
				res = responseWithFlavour(dontknowMsg+" "+correctAnswer2+". "+buildString(sumMsg, String.valueOf(sum), " ")+" "+continueMsg,0);
			*/	
				
		  } else {
					
					recState = RecognitionState.YesNoQuizLevelOne;
					res = responseWithFlavour(wrongMsg+" "+correctAnswer2+". "+buildString(sumMsg, String.valueOf(sum), " ")+" "+continueMsg, 7);
					// wrongVocMsg+" "+correctAnswer+". "+continueMsg,7
				}
			} 
		return res;
	}
	
	/*im Einzel Quiz Level2*/
	private SpeechletResponse evaluateAnswerQuizLevelTwo(String userRequest) {
		SpeechletResponse res = null;
		recognizeUserIntent(userRequest);{
			logger.info("User answer ="+ userRequest.toLowerCase()+ "/correct answer="+correctAnswer2);
			if (userRequest.toLowerCase().equals(correctAnswer2.toLowerCase())) {
				logger.info("User answer recognized as correct.");
					increaseSum();
					if (sum >= 100) {
						recState = RecognitionState.YesNoQuizLevelThree;
						res = responseWithFlavour(correctMsg+" "+buildString(sumMsg, String.valueOf(sum), " ")+" "+continueEinzelQuizLevelThreeMsg, 9);
					} else {
						recState = RecognitionState.YesNoQuizLevelTwo;
						res = responseWithFlavour(correctMsg+" "+buildString(sumMsg, String.valueOf(sum), " ")+" "+continueMsg, 8);
					}
				} else {
					recState = RecognitionState.YesNoQuizLevelTwo;
					res = responseWithFlavour(wrongMsg+" "+correctAnswer2+". "+buildString(sumMsg, String.valueOf(sum), " ")+" "+continueMsg, 7);
				}
			} 
		return res;
	}
	
	/*im Einzel Quiz Level3*/
	private SpeechletResponse evaluateAnswerQuizLevelThree(String userRequest) {
		SpeechletResponse res = null;
		recognizeUserIntent(userRequest);{
			logger.info("User answer ="+ userRequest.toLowerCase()+ "/correct answer="+correctAnswer2);
			if (userRequest.toLowerCase().equals(correctAnswer2.toLowerCase())) {
				logger.info("User answer recognized as correct.");
					increaseSum();
					if (sum >= 150) {
						recState = RecognitionState.YesNoQuizLevelEnd;
						res = responseWithFlavour(correctMsg+" "+buildString(sumMsg, String.valueOf(sum), " ")+" "+continueEinzelQuizEndMsg, 9);
					} else {
						recState = RecognitionState.YesNoQuizLevelThree;
						res = responseWithFlavour(correctMsg+" "+buildString(sumMsg, String.valueOf(sum), " ")+" "+continueMsg, 8);
					}
				} else {
					recState = RecognitionState.YesNoQuizLevelThree;
					res = responseWithFlavour(wrongMsg+" "+correctAnswer2+". "+buildString(sumMsg, String.valueOf(sum), " ")+" "+continueMsg, 7);
				}
			} 
		return res;
	}

	
	/*Mehrspielermodus: Antwort von Spieler eins in Level eins*/
	private SpeechletResponse evaluateAnswerTwo(String userRequest) {
		SpeechletResponse res = null;
		recognizeUserIntent(userRequest);{
				logger.info("User answer ="+ userRequest.toLowerCase()+ "/correct answer="+correctAnswer);
				if (userRequest.toLowerCase().equals(correctAnswer.toLowerCase())) {
					logger.info("User answer recognized as correct.");
					increaseSum2();
					if (sum2 == 40) {
						recState = RecognitionState.YesNoLevel;
						res = responseWithFlavour(correctMsg+" "+buildString2(sumTwoMsg, String.valueOf(sum2), " ")+" "+buildString3(sumThreeMsg, String.valueOf(sum3), " ")+" "+playerOneWins+" "+continueLevelMsg, 8);
					} else {
						recState = RecognitionState.YesNoTwo;
						res = responseWithFlavour(correctMsg+" "+buildString2(sumTwoMsg, String.valueOf(sum2), " ")+" "+buildString3(sumThreeMsg, String.valueOf(sum3), " ")+" "+continueMsg, 8);
					}
				} else {
					recState = RecognitionState.YesNoTwo;
					res = responseWithFlavour(wrongMsg+" "+correctAnswer+". "+" "+buildString2(sumTwoMsg, String.valueOf(sum2), " ")+" "+buildString3(sumThreeMsg, String.valueOf(sum3), " ")+" "+continueMsg, 7);
				}
			} 
				return res;
	}
	
	/*Mehrspielermodus: Antwort von Spieler eins in Level zwei*/
	private SpeechletResponse evaluateAnswerFour(String userRequest) {
		SpeechletResponse res = null;
		recognizeUserIntent(userRequest);{
				logger.info("User answer ="+ userRequest.toLowerCase()+ "/correct answer="+correctAnswer);
				if (userRequest.toLowerCase().equals(correctAnswer.toLowerCase())) {
					logger.info("User answer recognized as correct.");
					increaseSum4();
					if (sum4 >= 40) {
						recState = RecognitionState.YesNoLevelTwo;
						res = responseWithFlavour(correctMsg+" "+buildString2(sumTwoMsg, String.valueOf(sum2+sum4), " ")+" "+buildString3(sumThreeMsg, String.valueOf(sum3+sum5), " ")+" "+playerOneWins+" "+continueLevelTwoMsg, 8);
					} else {
						recState = RecognitionState.YesNoLevel;
						res = responseWithFlavour(correctMsg+" "+buildString2(sumTwoMsg, String.valueOf(sum2+sum4), " ")+" "+buildString3(sumThreeMsg, String.valueOf(sum3+sum5), " ")+" "+continueMsg, 8);
					}
				} else {
					decreaseSum4();
					increaseSum5();
					recState = RecognitionState.YesNoLevel;
					res = responseWithFlavour(wrongMsg+" "+correctAnswer+". "+buildString2(sumTwoMsg, String.valueOf(sum2+sum4), " ")+" "+buildString3(sumThreeMsg, String.valueOf(sum3+sum5), " ")+" "+continueMsg, 7);
				}
			} 
				return res;
	}
	
	/*Mehrspielermodus: Antwort von Spieler eins in Level drei*/
	private SpeechletResponse evaluateAnswerSix(String userRequest) {
		SpeechletResponse res = null;
		recognizeUserIntent(userRequest);{
				logger.info("User answer ="+ userRequest.toLowerCase()+ "/correct answer="+correctAnswer);
				if (userRequest.toLowerCase().equals(correctAnswer.toLowerCase())) {
					logger.info("User answer recognized as correct.");
					increaseSum6();
					if (sum2+sum4+sum6>sum3+sum5+sum7 & sum6 >= 40) {
						recState = RecognitionState.AgainOrMenu;
						res = responseWithFlavour(correctMsg+" "+buildString2(sumTwoMsg, String.valueOf(sum2+sum4+sum6), " ")+" "+buildString3(sumThreeMsg, String.valueOf(sum3+sum5+sum7), " ")+" "+playerOneWinsGame+" "+againOrMenuMsg, 8);
					} else {
						recState = RecognitionState.YesNoLevelTwo;
						res = responseWithFlavour(correctMsg+" "+buildString2(sumTwoMsg, String.valueOf(sum2+sum4+sum6), " ")+" "+buildString3(sumThreeMsg, String.valueOf(sum3+sum5+sum7), " ")+" "+continueMsg, 8);
					}
				} else {
					recState = RecognitionState.YesNoLevelTwo;
					res = responseWithFlavour(wrongMsg+" "+correctAnswer+". "+buildString2(sumTwoMsg, String.valueOf(sum2+sum4+sum6), " ")+" "+buildString3(sumThreeMsg, String.valueOf(sum3+sum5+sum7), " ")+" "+continueMsg, 7);
				}
			} 
				return res;
	}
	
	/*Mehrspielermodus: Antwort von Spieler zwei in Level eins*/
	private SpeechletResponse evaluateAnswerThree(String userRequest) {
		SpeechletResponse res = null;
		recognizeUserIntent(userRequest);{
				logger.info("User answer ="+ userRequest.toLowerCase()+ "/correct answer="+correctAnswer);
				
				if (userRequest.toLowerCase().equals(correctAnswer.toLowerCase())) {
					logger.info("User answer recognized as correct.");
					increaseSum3();
					if (sum3 >= 40) {
						recState = RecognitionState.YesNoLevel;
						res = responseWithFlavour(correctMsg+" "+buildString2(sumTwoMsg, String.valueOf(sum2), " ")+" "+buildString3(sumThreeMsg, String.valueOf(sum3), " ")+" "+playerTwoWins+" "+continueLevelMsg, 8);
					} else {
						recState = RecognitionState.YesNoTwo;
						res = responseWithFlavour(correctMsg+" "+buildString2(sumTwoMsg, String.valueOf(sum2), " ")+" "+buildString3(sumThreeMsg, String.valueOf(sum3), " ")+" "+continueMsg, 8);
					}
				} else {
					recState = RecognitionState.YesNoTwo;
					res = responseWithFlavour(wrongMsg+" "+correctAnswer+". "+buildString2(sumTwoMsg, String.valueOf(sum2), " ")+" "+buildString3(sumThreeMsg, String.valueOf(sum3), " ")+" "+continueMsg, 7);
				}
			} 
				return res;
	}
	
	/*Mehrspielermodus: Antwort von Spieler zwei in Level zwei*/
	private SpeechletResponse evaluateAnswerFive(String userRequest) {
		SpeechletResponse res = null;
		recognizeUserIntent(userRequest);{
				logger.info("User answer ="+ userRequest.toLowerCase()+ "/correct answer="+correctAnswer);
				if (userRequest.toLowerCase().equals(correctAnswer.toLowerCase())) {
					logger.info("User answer recognized as correct.");
					increaseSum5();
					if (sum5 >= 40) {
						recState = RecognitionState.YesNoLevelTwo;
						res = responseWithFlavour(correctMsg+" "+buildString2(sumTwoMsg, String.valueOf(sum2+sum4), " ")+" "+buildString3(sumThreeMsg, String.valueOf(sum3+sum5), " ")+" "+playerTwoWins+" "+continueLevelTwoMsg, 8);
					} else {
						recState = RecognitionState.YesNoLevel;
						res = responseWithFlavour(correctMsg+" "+buildString2(sumTwoMsg, String.valueOf(sum2+sum4), " ")+" "+buildString3(sumThreeMsg, String.valueOf(sum3+sum5), " ")+" "+continueMsg, 8);
					}
				} else {
					decreaseSum5();
					increaseSum4();
					recState = RecognitionState.YesNoLevel;
					res = responseWithFlavour(wrongMsg+" "+correctAnswer+". "+buildString2(sumTwoMsg, String.valueOf(sum2+sum4), " ")+" "+buildString3(sumThreeMsg, String.valueOf(sum3+sum5), " ")+" "+continueMsg, 7);
				}
			} 
				return res;
	}
	
	/*Mehrspielermodus: Antwort von Spieler zwei in Level drei*/
	private SpeechletResponse evaluateAnswerSeven(String userRequest) {
		SpeechletResponse res = null;
		recognizeUserIntent(userRequest);{
				logger.info("User answer ="+ userRequest.toLowerCase()+ "/correct answer="+correctAnswer);
				if (userRequest.toLowerCase().equals(correctAnswer.toLowerCase())) {
					logger.info("User answer recognized as correct.");
					increaseSum7();
					if (sum3+sum5+sum7>sum2+sum4+sum6 & sum7 >= 40) {
						recState = RecognitionState.AgainOrMenu;
						res = responseWithFlavour(correctMsg+" "+buildString2(sumTwoMsg, String.valueOf(sum2+sum4+sum6), " ")+" "+buildString3(sumThreeMsg, String.valueOf(sum3+sum5+sum7), " ")+" "+playerTwoWinsGame+" "+againOrMenuMsg, 8);
					} else {
						recState = RecognitionState.YesNoLevelTwo;
						res = responseWithFlavour(correctMsg+" "+buildString2(sumTwoMsg, String.valueOf(sum2+sum4+sum6), " ")+" "+buildString3(sumThreeMsg, String.valueOf(sum3+sum5+sum7), " ")+" "+continueMsg, 8);
					}
				} else {
					recState = RecognitionState.YesNoLevelTwo;
					res = responseWithFlavour(wrongMsg+" "+correctAnswer+". "+buildString2(sumTwoMsg, String.valueOf(sum2+sum4+sum6), " ")+" "+buildString3(sumThreeMsg, String.valueOf(sum3+sum5+sum7), " ")+" "+continueMsg, 7);
				}
			} 
				return res;
	}


	private void increaseSum() {
		switch(sum){
		case 0: sum = 10; break;
		case 10: sum = 20; break;
		case 20: sum = 30; break;
		case 30: sum = 40; break;
		case 40: sum = 50; break;
		case 50: sum = 60; break;
		case 60: sum = 70; break;
		case 70: sum = 80; break;
		case 80: sum = 90; break;
		case 90: sum = 100; break;
		case 100: sum = 110; break;
		case 110: sum = 120; break;
		case 120: sum = 130; break;
		case 130: sum = 140; break;
		case 140: sum = 150; break;
		}
	}
	
	private void increaseSum2() {
		switch(sum2){
		case 0: sum2 = 10; break;
		case 10: sum2 = 20; break;
		case 20: sum2 = 30; break;
		case 30: sum2 = 40; break;
		case 40: sum2 = 50; break;
		case 50: sum2 = 60; break;
		case 60: sum2 = 70; break;
		case 70: sum2 = 80; break;
		case 80: sum2 = 90; break;
		case 90: sum2 = 100; break;
		case 100: sum2 = 110; break;
		case 110: sum2 = 120; break;
		case 120: sum2 = 130; break;
		case 130: sum2 = 140; break;
		case 140: sum2 = 150; break;
		}
	}
	
	private void increaseSum3() {
		switch(sum3){
		case 0: sum3 = 10; break;
		case 10: sum3 = 20; break;
		case 20: sum3 = 30; break;
		case 30: sum3 = 40; break;
		case 40: sum3 = 50; break;
		case 50: sum3 = 60; break;
		case 60: sum3 = 70; break;
		case 70: sum3 = 80; break;
		case 80: sum3 = 90; break;
		case 90: sum3 = 100; break;
		case 100: sum3 = 110; break;
		case 110: sum3 = 120; break;
		case 120: sum3 = 130; break;
		case 130: sum3 = 140; break;
		case 140: sum3 = 150; break;
		}
	}
	
	private void increaseSum4() {
		switch(sum4){
		case 0: sum4 = 10; break;
		case 10: sum4 = 20; break;
		case 20: sum4 = 30; break;
		case 30: sum4 = 40; break;
		case 40: sum4 = 50; break;
		case 50: sum4 = 60; break;
		case 60: sum4 = 70; break;
		case 70: sum4 = 80; break;
		case 80: sum4 = 90; break;
		case 90: sum4 = 100; break;
		case 100: sum4 = 110; break;
		case 110: sum4 = 120; break;
		case 120: sum4 = 130; break;
		case 130: sum4 = 140; break;
		case 140: sum4 = 150; break;
		}
	}
	
	private void decreaseSum4() {
		switch(sum4){
		case 10: sum4 = 0; break;
		case 20: sum4 = 10; break;
		case 30: sum4 = 20; break;
		case 40: sum4 = 30; break;
		case 50: sum4 = 40; break;
		case 60: sum4 = 50; break;
		case 70: sum4 = 60; break;
		case 80: sum4 = 70; break;
		case 90: sum4 = 80; break;
		case 100: sum4 = 90; break;
		case 110: sum4 = 100; break;
		case 120: sum4 = 110; break;
		case 130: sum4 = 120; break;
		case 140: sum4 = 130; break;
		case 150: sum4 = 140; break;
		}
	}
	
	private void increaseSum5() {
		switch(sum5){
		case 0: sum5 = 10; break;
		case 10: sum5 = 20; break;
		case 20: sum5 = 30; break;
		case 30: sum5 = 40; break;
		case 40: sum5 = 50; break;
		case 50: sum5 = 60; break;
		case 60: sum5 = 70; break;
		case 70: sum5 = 80; break;
		case 80: sum5 = 90; break;
		case 90: sum5 = 100; break;
		case 100: sum5 = 110; break;
		case 110: sum5 = 120; break;
		case 120: sum5 = 130; break;
		case 130: sum5 = 140; break;
		case 140: sum5 = 150; break;
		}
	}
	
	private void decreaseSum5() {
		switch(sum5){
		case 10: sum5 = 0; break;
		case 20: sum5 = 10; break;
		case 30: sum5 = 20; break;
		case 40: sum5 = 30; break;
		case 50: sum5 = 40; break;
		case 60: sum5 = 50; break;
		case 70: sum5 = 60; break;
		case 80: sum5 = 70; break;
		case 90: sum5 = 80; break;
		case 100: sum5 = 90; break;
		case 110: sum5 = 100; break;
		case 120: sum5 = 110; break;
		case 130: sum5 = 120; break;
		case 140: sum5 = 130; break;
		case 150: sum5 = 140; break;
		}
	}
	
	private void increaseSum6() {
		switch(sum6){
		case 0: sum6 = 10; break;
		case 10: sum6 = 20; break;
		case 20: sum6 = 30; break;
		case 30: sum6 = 40; break;
		case 40: sum6 = 50; break;
		case 50: sum6 = 60; break;
		case 60: sum6 = 70; break;
		case 70: sum6 = 80; break;
		case 80: sum6 = 90; break;
		case 90: sum6 = 100; break;
		case 100: sum6 = 110; break;
		case 110: sum6 = 120; break;
		case 120: sum6 = 130; break;
		case 130: sum6 = 140; break;
		case 140: sum6 = 150; break;
		}
	}
	
	private void increaseSum7() {
		switch(sum7){
		case 0: sum7 = 10; break;
		case 10: sum7 = 20; break;
		case 20: sum7 = 30; break;
		case 30: sum7 = 40; break;
		case 40: sum7 = 50; break;
		case 50: sum7 = 60; break;
		case 60: sum7 = 70; break;
		case 70: sum7 = 80; break;
		case 80: sum7 = 90; break;
		case 90: sum7 = 100; break;
		case 100: sum7 = 110; break;
		case 110: sum7 = 120; break;
		case 120: sum7 = 130; break;
		case 130: sum7 = 140; break;
		case 140: sum7 = 150; break;
		}
	}
	
	
	void recognizeUserIntent(String userRequest) {
		userRequest = userRequest.toLowerCase();
		String pattern1 = "(.*)?(\\bresume\\b)(.*)?";
		String pattern2 = "(.*)?(\\b(one)|(1)\\b)(.*)?";
		String pattern3 = "(.*)?(\\b(two)|(2)\\b)(.*)?";
		String pattern4 = "(.*)?(\\b(vocabulary)|(vocab)\\b)(.*)?";
		String pattern5 = "(.*)?(\\bquiz\\b)(.*)?";
		String pattern6 = "(.*)?(\\beasy\\b)(.*)?";
		String pattern7 = "(.*)?(\\bbasics\\b)(.*)?";
		String pattern8 = "(.*)?(\\bexpressions\\b)(.*)?";
		String pattern9 = "(.*)?(\\bone\\b)(.*)?";
		String pattern10 = "(.*)?(\\btwo\\b)(.*)?";
		String pattern11 = "(.*)?(\\bmenu\\b)(.*)?";
		String pattern12 = "(.*)?(next)?(\\blevel\\b)(.*)?";
		String pattern13 = "(.*)?(\\byes\\b)(.*)?";
		String pattern14 = "(.*)?(\\bno\\b)(.*)?";
		String pattern15 = "(.*)?(\\bquiz\\b)(.*)?";
		String pattern16 = "(.*)?(\\bquit\\b)(.*)?";
		String pattern17 = "(.*)?(\\bdifficulty\\b)(.*)?";
		String pattern18 = "(.*)?(\\bvocab\\b)(.*)?";
		String pattern19 = "(.*)?(\\blevel\\sone\\b)(.*)?";
		String pattern20 = "(.*)?(\\blevel\\stwo\\b)(.*)?";
		//String pattern21 = "(I)?(don´t)?(know)?(what)?(is)?(the)?(correct)?(right)?(answer)?";
		String pattern22 = "(.*)?(\\bagain\\b)(.*)?";
		
		
		
		Pattern p1 = Pattern.compile(pattern1);
		Matcher m1 = p1.matcher(userRequest);
		Pattern p2 = Pattern.compile(pattern2);
		Matcher m2= p2.matcher(userRequest);
		Pattern p3 = Pattern.compile(pattern3);
		Matcher m3= p3.matcher(userRequest);
		Pattern p4 = Pattern.compile(pattern4);
		Matcher m4= p4.matcher(userRequest);
		Pattern p5 = Pattern.compile(pattern5);
		Matcher m5= p5.matcher(userRequest);
		Pattern p6 = Pattern.compile(pattern6);
		Matcher m6= p6.matcher(userRequest);
		Pattern p7 = Pattern.compile(pattern7);
		Matcher m7= p7.matcher(userRequest);
		Pattern p8 = Pattern.compile(pattern8);
		Matcher m8= p8.matcher(userRequest);
		Pattern p9 = Pattern.compile(pattern9);
		Matcher m9= p9.matcher(userRequest);
		Pattern p10 = Pattern.compile(pattern10);
		Matcher m10= p10.matcher(userRequest);
		Pattern p11 = Pattern.compile(pattern11);
		Matcher m11= p11.matcher(userRequest);
		Pattern p12 = Pattern.compile(pattern12);
		Matcher m12= p12.matcher(userRequest);
		Pattern p13 = Pattern.compile(pattern13);
		Matcher m13= p13.matcher(userRequest);
		Pattern p14 = Pattern.compile(pattern14);
		Matcher m14= p14.matcher(userRequest);
		Pattern p15 = Pattern.compile(pattern15);
		Matcher m15= p15.matcher(userRequest);
		Pattern p16 = Pattern.compile(pattern16);
		Matcher m16= p16.matcher(userRequest);
		Pattern p17 = Pattern.compile(pattern17);
		Matcher m17 = p17.matcher(userRequest);
		Pattern p18 = Pattern.compile(pattern18);
		Matcher m18 = p18.matcher(userRequest);
		Pattern p19 = Pattern.compile(pattern19);
		Matcher m19 = p19.matcher(userRequest);
		Pattern p20 = Pattern.compile(pattern20);
		Matcher m20 = p20.matcher(userRequest);
		//Pattern p21 = Pattern.compile(pattern21);
		//Matcher m21 = p21.matcher(userRequest);
		Pattern p22 = Pattern.compile(pattern22);
		Matcher m22 = p22.matcher(userRequest);
		
		
		if (m1.find()) {
			ourUserIntent = UserIntent.resume;
		} else if (m2.find()) {
			ourUserIntent = UserIntent.onne;
		} else if (m3.find()) {
			ourUserIntent = UserIntent.twwo;
		} else if (m4.find()) {
			ourUserIntent = UserIntent.vocabulary;
		} else if (m5.find()) {
			ourUserIntent = UserIntent.quiz;
		} else if (m6.find()) {
			ourUserIntent = UserIntent.easy;
		} else if (m7.find()) {
			ourUserIntent = UserIntent.basics;
		} else if (m8.find()) {
			ourUserIntent = UserIntent.expressions;
		} else if (m9.find()) {
			ourUserIntent = UserIntent.playerone;
		} else if (m10.find()) {
			ourUserIntent = UserIntent.playertwo;
		} else if (m11.find()) {
			ourUserIntent = UserIntent.menu;
		} else if (m12.find()) {
			ourUserIntent = UserIntent.nextlevel;
		} else if (m13.find()) {
			ourUserIntent = UserIntent.yess;
		} else if (m14.find()) {
			ourUserIntent = UserIntent.no;
		} else if (m15.find()) {
			ourUserIntent = UserIntent.quiz;
		} else if (m16.find()) {
			ourUserIntent = UserIntent.quit;
		} else if (m17.find()) {
			ourUserIntent = UserIntent.difficulty;
		} else if (m18.find()) {
			ourUserIntent = UserIntent.vocab;
		} else if (m19.find()) {
			ourUserIntent = UserIntent.levelone;
		} else if (m20.find()) {
			ourUserIntent = UserIntent.leveltwo;
		//} else if (m21.find()) {
		//	ourUserIntent = UserIntent.Answer;
		} else if (m22.find()) {
			ourUserIntent = UserIntent.again;
		} else {
			ourUserIntent = UserIntent.Error;
		}
		logger.info("set ourUserIntent to " +ourUserIntent);
	}

	 
	/**
	 * formats the text in weird ways
	 * @param text
	 * @param i
	 * @return
	 */
	private SpeechletResponse responseWithFlavour(String text, int i) {

		SsmlOutputSpeech speech = new SsmlOutputSpeech();
		switch(i){ 
		// normal:
		case 0: 
			speech.setSsml("<speak>" + text + "</speak>");
			break; 
		case 1: 
			speech.setSsml("<speak><emphasis level=\"strong\">" + text + "</emphasis></speak>");
			break; 
		case 2: 
			String half1=text.split(" ")[0];
			String[] rest = Arrays.copyOfRange(text.split(" "), 1, text.split(" ").length);
			speech.setSsml("<speak>"+half1+"<break time=\"3s\"/>"+ StringUtils.join(rest," ") + "</speak>");
			break; 
		case 3: 
			String firstNoun="erstes Wort buchstabiert";
			String firstN=text.split(" ")[3];
			speech.setSsml("<speak>"+firstNoun+ "<say-as interpret-as=\"spell-out\">"+firstN+"</say-as>"+"</speak>");
			break; 
	
		case 4: 
			speech.setSsml("<speak><audio src='soundbank://soundlibrary/transportation/amzn_sfx_airplane_takeoff_whoosh_01'/></speak>");
			break;
			
		// in Kombination mit WelcomeMsg - Menu (GAMESHOW INTRO SOUND):
		case 5: 
			speech.setSsml("<speak> <audio src=\"soundbank://soundlibrary/ui/gameshow/amzn_ui_sfx_gameshow_intro_01\"/> <amazon:emotion name=\"excited\" intensity=\"high\">" + text + "</amazon:emotion> </speak>");
			break;
			
		//  What does the german word ++DEUTSCHE AUSSPRACHE++  mean in english?
		case 6: 
			speech.setSsml("<speak> What does the german word <voice name=\"Marlene\"><lang xml:lang=\"de-DE\">" + question + "</lang> </voice>mean in english?</speak>");
			break;	
			
		// wrong feedback sound	
		case 7: 
			speech.setSsml("<speak><audio src=\"soundbank://soundlibrary/ui/gameshow/amzn_ui_sfx_gameshow_negative_response_02\"/> <amazon:emotion name=\"excited\" intensity=\"low\"> " + text + "</amazon:emotion> </speak>");
			break;
		// correct feedback sound	
		case 8: 
			speech.setSsml("<speak><audio src=\"soundbank://soundlibrary/ui/gameshow/amzn_ui_sfx_gameshow_positive_response_01\"/> <amazon:emotion name=\"excited\" intensity=\"low\"> " + text + "</amazon:emotion> </speak>");
			break;
			
		// new level sound
		case 9: 
			speech.setSsml("<speak><audio src=\"soundbank://soundlibrary/ui/gameshow/amzn_ui_sfx_gameshow_bridge_02\"/> <amazon:emotion name=\"excited\" intensity=\"medium\"> " + text + "</amazon:emotion> </speak>");
						break;	
						
		//  Two player mode. What does the german word ++DEUTSCHE AUSSPRACHE++  mean in english?
		case 10: 
			speech.setSsml("<speak> You're in two player mode. Please clarify who wants to be player one and who wants to be player two. If you think you know the correct answer, say you're player number. You will get points if your answer is correct. Let's begin! What does the german word <voice name=\"Marlene\"><lang xml:lang=\"de-DE\">" + question + "</lang> </voice>mean in english?</speak>");
						break;	
						
			
		default: 
			speech.setSsml("<speak><amazon:effect name=\"whispered\">" + text + "</amazon:effect></speak>");
		} 
		
		Reprompt rep = new Reprompt();
		rep.setOutputSpeech(speech);

		return SpeechletResponse.newAskResponse(speech, rep);
	}

	//anwendung wird anschliessend beendet:in Kombination mit goodbyeMsg (GAMESHOW OUTRO SOUND):
	
	private SpeechletResponse responseWithFlavour2(String text, int i) {

		SsmlOutputSpeech speech = new SsmlOutputSpeech();
		switch(i){ 
		case 0: 
			speech.setSsml("<speak><amazon:emotion name=\"excited\" intensity=\"medium\">" + text + "</amazon:emotion><audio src=\"soundbank://soundlibrary/ui/gameshow/amzn_ui_sfx_gameshow_outro_01\" /></speak>");
			break; 
		
		default: 
			speech.setSsml("<speak><amazon:effect name=\"whispered\">Das ist ein Test lol!</amazon:effect></speak>");
		} 

		return SpeechletResponse.newTellResponse(speech);
		
		
	}



	@Override
	public void onSessionEnded(SpeechletRequestEnvelope<SessionEndedRequest> requestEnvelope)
	{
		logger.info("Alexa session ends now");
	}



	/**
	 * Tell the user something - the Alexa session ends after a 'tell'
	 */
	private SpeechletResponse response(String text)
	{
		// Create the plain text output.
		PlainTextOutputSpeech speech = new PlainTextOutputSpeech();
		speech.setText(text);

		return SpeechletResponse.newTellResponse(speech);
	}

	/**
	 * A response to the original input - the session stays alive after an ask request was send.
	 *  have a look on https://developer.amazon.com/de/docs/custom-skills/speech-synthesis-markup-language-ssml-reference.html
	 * @param text
	 * @return
	 */
	private SpeechletResponse askUserResponse(String text)
	{
		SsmlOutputSpeech speech = new SsmlOutputSpeech();
		speech.setSsml("<speak>" + text + "</speak>");

		// reprompt after 8 seconds
		SsmlOutputSpeech repromptSpeech = new SsmlOutputSpeech();
		repromptSpeech.setSsml("<speak><emphasis level=\"strong\">Hey!</emphasis> Bist du noch da?</speak>");

		Reprompt rep = new Reprompt();
		rep.setOutputSpeech(repromptSpeech);

		return SpeechletResponse.newAskResponse(speech, rep);
	}
}
