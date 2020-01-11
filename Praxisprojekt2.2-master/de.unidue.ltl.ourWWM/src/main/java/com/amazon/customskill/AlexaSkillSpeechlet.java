/**
    Copyright 2014-2015 Amazon.com, Inc. or its affiliates. All Rights Reserved.

    Licensed under the Apache License, Version 2.0 (the "License"). You may not use this file except in compliance with the License. A copy of the License is located at

        http://aws.amazon.com/apache2.0/

    or in the "license" file accompanying this file. This file is distributed on an "AS IS" BASIS, WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied. See the License for the specific language governing permissions and limitations under the License.
 */
package com.amazon.customskill;

import java.util.ArrayList;



import java.util.Arrays;
import java.util.List;
import java.util.Random;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
import java.util.stream.IntStream;

import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

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


/*
 * This class is the actual skill. Here you receive the input and have to produce the speech output. 
 */
public class AlexaSkillSpeechlet
implements SpeechletV2
{
	static Logger logger = LoggerFactory.getLogger(AlexaSkillSpeechlet.class);

	public static String userRequest;

	private static int sum;
	private static int sum2;
	private static int sum3;
	private static int sum4;
	private static int sum5;
	private static int sum6;
	private static int sum7;
	private static int questions;
	private static int questions1;
	private static int questions11;
	private static int questions2;
	private static int questions3;
	private static int questions4;
	private static String question = "";
	private static String question1 = "";
	private static String question11 = "";
	private static String question2 = "";
	private static String question3 = "";
	private static String question4 = "";
	private static String correctAnswer = "";
	private static String correctAnswer1 = "";
	private static String correctAnswer11 = "";
	private static String correctAnswer2 = "";
	private static String correctAnswer3 = "";
	private static String correctAnswer4 = "";
	private static enum RecognitionState {YesNoQuizLevelEnd, YesNoQuizLevelOne, YesNoQuizLevelTwo, YesNoQuizLevelThree, YesNoVokabelnEasy, YesNoVokabelnMedium, YesNoVokabelnHard, AnswerQuizLevelOne, AnswerQuizLevelTwo, AnswerQuizLevelThree, AnswerVokabelnEasy, AnswerVokabelnMedium, AnswerVokabelnHard, Answer, AnswerTwo, AnswerThree, AnswerFour, AnswerFive, AnswerSix, AnswerSeven, YesNo, YesNoTwo, YesNoLevel, YesNoLevelTwo, OneTwo, VokabelQuiz, Vokabel, WhichPlayer, WhichPlayerThree, WhichPlayerFour, AgainOrMenu, Weiterquizzen, SingleQuiz, YesNoQuiz, YesNoVokabeln, AnswerVokabeln, AnswerQuiz, Menu};
	private RecognitionState recState;
	private static enum UserIntent {hand, hey, levelone, leveltwo, difficulty, bone, onne, twwo, yess, again, banana, menu, bye, playerone, playertwo, vocabulary, resume, quiz, yes, no, leave, quit, hello, tree, light, now, maybe, today, easy, medium, hard, moin, nextlevel, Error, Quiz, food, head, hair, leg, sun, always, water, table, city, stairs, haircolour, wheel, bellybutton, broken, contract, community, candle, field, gale, giveup, microwave, pillow, policy, balance, acquaintance, bossy, confident, generous, 
		mother, inlaw, moody, reliable, accountancy, apply, fluently, insist, representative, 
		smoothly, bewillingto, middleclass, motherinlaw, vocab};
		
	UserIntent ourUserIntent;

	
	
	
	
	
	/* static String welcomeMsg = "<speak><amazon:emotion name=\"excited\"intensity=\"medium\">.Five seconds till lift off!</amazon:emotion></speak>"; */
	
	/* static String welcomeMsg = "<speak><amazon:effect name=\"whispered\">\"das ist ein test!\"</amazon:effect></speak>"; */
	
	static String welcomeMsg = "Hello and welcome at Quizzitch. How many players want to play? One or two players?";
	static String singleMsg = "You're in single mode. Do you want to train vocabulary first or starting a quiz?";
	static String multiMsg = "You're in two player mode. Please clarify who wants to be player one and who wants to be player two. If you think you know the correct answer, say you're player number. You will get points if your answer is correct. Let's begin!";
	static String difficultyMsg = "Please choose between the difficulty levels easy, medium and hard.";
	static String singleQuizMsg = "Welcome to the single quiz. Let's start with the first question.";
	static String wrongMsg = "That's wrong.";
	static String correctMsg = "Well done, that's correct!";
	static String resume1Msg = "Do you want to resume playing?";
	static String congratsMsg = "Congratulations! You've won one million points.";
	static String goodbyeMsg = "I hope to hear from you soon, good bye!";
	static String sumMsg = "You've {replacement} points.";
	static String sumTwoMsg = "The score is {replacement3}";
	static String sumThreeMsg = "to {replacement5}.";
	static String errorYesNoMsg = "Sorry I did not unterstand that. Say resume or quit.";
	static String errorAgainOrMenuMsg = "Sorry I did not unterstand that. Please say again, menu or quit.";
	static String errorAnswerMsg = "Sorry I did not unterstand that. Please mention your answer again.";
	static String errorOneTwoMsg = "Please choose between one or two";
	static String errorVokabelQuizMsg = "Unfortunately I did not understand that. Say vocabulary or quiz.";
	static String errorVokabelMsg = "In which difficulty level do you want to train your vocabulary? You can choose between easy, medium and hard.";
	static String VokabelLeicht = "Welcome to the easy vocab trainer";
	static String VokabelMittel = "Welcome to the medium vocab trainer";
	static String VokabelSchwer = "Welcome to the hard vocab trainer";
	static String errorSpielereinszweiMsg = "Please say your player number first.";
	static String SpielerEinsMsg = "Player one was faster. What's your answer?";
	static String SpielerEinsKurzMsg = "Player one?";
	static String SpielerZweiMsg = "Player two was faster. What's your answer?";
	static String SpielerZweiKurzMsg = "Player two?";
	static String resumeLevelMsg = "Level two is up. Do you want to continue?";
	static String resumeLevelTwoMsg = "Level three is up. Do you want to continue?";
	static String resumeEinzelQuizLevelTwoMsg = "You can resume with level two. Do you want to jump to the second level?";
	static String resumeEinzelQuizLevelThreeMsg = "You can resume with level three. Do you want to jump to the third level?";
	static String resumeEinzelQuizEndMsg = "Congrats, you made it! You passed all levels! Do you want to resume, change level or back to the menu?";
	static String playerOneWins = "Player one has won this round.";
	static String playerTwoWins = "Player two has won this round.";
	static String playerOneWinsGame = "Player one has won the game.";
	static String playerTwoWinsGame = "Player two has won the game.";
	static String againOrMenuMsg = "Do you want to play again, go back to the menu or quit?";
	static String weiterquizzenMsg = "Do you want to play a quiz instead or quit the app?";
	static String errorWeiterquizzen = "Sorry I did not understand that. Please say quiz or quit the app.";
	static String weiterVokabelnMsg = "Do you want to the some vocabulary instead or quit the app?";
	


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
		questions = 1;
		questions1 = 1;
		questions11 = 1;
		questions2 = 1;
		questions3 = 1;
		questions4 = 1;
		recState = RecognitionState.OneTwo;
	}

	@Override
	public SpeechletResponse onLaunch(SpeechletRequestEnvelope<LaunchRequest> requestEnvelope)
	{

		return askUserResponse(welcomeMsg);
		// return responseWithFlavour(welcomeMsg, 5);
		
	
		
	}
	
	private void selectQuestion() {
		Random r = new Random();
		int questions = r.nextInt(15);
		switch(questions){
		
		case 1: question = "Hallo bedeutet auf englisch hello. Say hallo in english."; correctAnswer = "hello"; correctAnswer = "hey"; break;
		case 2: question = "What does the german word baum in english mean?"; correctAnswer = "tree"; break;
		case 3: question = "jetzt bedeutet auf englisch now. Sage jetzt auf englisch."; correctAnswer = "now"; break;
		case 4: question = "vielleicht bedeutet auf englisch maybe. Sage vielleicht auf englisch."; correctAnswer = "maybe"; break;
		case 5: question = "heute bedeutet auf englisch today. Sage heute auf englisch."; correctAnswer = "today"; break;
		case 6: question = "das Essen bedeutet auf englisch food. Sage Essen auf englisch"; correctAnswer = "food"; break;
		case 7: question = "Kopf bedeutet auf englisch head. Sage Kopf auf englisch."; correctAnswer = "head"; break;
		case 8: question = "Hand bedeutet auf englisch hand. Sage Hand auf englisch"; correctAnswer = "hand"; break;
		case 9: question = "Haare bedeutet auf englisch Hair.Sage Haare auf englisch."; correctAnswer = "hair"; break;
		case 10: question = "Bein bedeutet auf englisch leg. Sage Bein auf englisch."; correctAnswer = "leg"; break;
		case 11: question = "Sonne bedeutet auf englisch sun. Sage Sonne auf englisch."; correctAnswer = "sun"; break;
		case 12: question = "Immer bedeutet auf englisch always.Sage immer auf englisch"; correctAnswer = "always"; break;
		case 13: question = "Wasser bedeutet auf englisch water.Sage Wasser auf englisch"; correctAnswer = "water"; break;
		case 14: question = "Tisch bedeutet auf englisch table. Sage Tisch auf englisch."; correctAnswer = "table"; break;
		case 15: question = "Stadt bedeutet auf englisch city. Sage Stadt auf englisch."; correctAnswer = "city"; break;
		}
	}
	
	private void selectQuestion1() {
		Random r = new Random();
		int questions1 = r.nextInt(16);
		switch(questions1){
		case 1: question1 = "Hallo bedeutet auf englisch hello. Sage hallo auf englisch."; correctAnswer1 = "hello"; break;
		case 2: question1 = "baum bedeutet auf englisch tree. Sage baum auf englisch."; correctAnswer1 = "tree"; break;
		case 3: question1 = "jetzt bedeutet auf englisch now. Sage jetzt auf englisch."; correctAnswer1 = "now"; break;
		case 4: question1 = "vielleicht bedeutet auf englisch maybe. Sage vielleicht auf englisch."; correctAnswer1 = "maybe"; break;
		case 5: question1 = "heute bedeutet auf englisch today. Sage heute auf englisch."; correctAnswer1 = "today"; break;
		case 6: question1 = "das Essen bedeutet auf englisch food.Sage Essen auf englisch"; correctAnswer1 = "food"; break;
		case 7: question1 = "Kopf bedeutet auf englisch head.Sage Kopf auf englisch."; correctAnswer1 = "head"; break;
		case 8: question1 = "Hand bedeutet auf englisch hand.Sage Hand auf englisch"; correctAnswer1 = "hand"; break;
		case 9: question1 = "Haare bedeutet auf englisch Hair.Sage Haare auf englisch."; correctAnswer1 = "hair"; break;
		case 10: question1 = "Bein bedeutet auf englisch leg. Sage Bein auf englisch."; correctAnswer1 = "leg"; break;
		case 11: question1 = "Sonne bedeutet auf englisch sun. Sage Sonne auf englisch."; correctAnswer1 = "sun"; break;
		case 12: question1 = "Immer bedeutet auf englisch always.Sage immer auf englisch"; correctAnswer1 = "always"; break;
		case 13: question1 = "Wasser bedeutet auf englisch water.Sage Wasser auf englisch"; correctAnswer1 = "water"; break;
		case 14: question1 = "Tisch bedeutet auf englisch table. Sage Tisch auf englisch."; correctAnswer1 = "table"; break;
		case 15: question1 = "Stadt bedeutet auf englisch city. Sage Stadt auf englisch."; correctAnswer1 = "city"; break;
		}
	}
	
	private void selectQuestion11() {
		Random r = new Random();
		int questions11 = r.nextInt(17);
		switch(questions11){
		case 1: question11 = "Hallo bedeutet auf englisch hello. Sage hallo auf englisch."; correctAnswer11 = "hello"; break;
		case 2: question11 = "baum bedeutet auf englisch tree. Sage baum auf englisch."; correctAnswer11 = "tree"; break;
		case 3: question11 = "jetzt bedeutet auf englisch now. Sage jetzt auf englisch."; correctAnswer11 = "now"; break;
		case 4: question11 = "vielleicht bedeutet auf englisch maybe. Sage vielleicht auf englisch."; correctAnswer11 = "maybe"; break;
		case 5: question11 = "heute bedeutet auf englisch today. Sage heute auf englisch."; correctAnswer11 = "today"; break;
		case 6: question11 = "das Essen bedeutet auf englisch food.Sage Essen auf englisch"; correctAnswer11 = "food"; break;
		case 7: question11 = "Kopf bedeutet auf englisch head.Sage Kopf auf englisch."; correctAnswer11 = "head"; break;
		case 8: question11 = "Hand bedeutet auf englisch hand.Sage Hand auf englisch"; correctAnswer11 = "hand"; break;
		case 9: question11 = "Haare bedeutet auf englisch Hair.Sage Haare auf englisch."; correctAnswer11 = "hair"; break;
		case 10: question11 = "Bein bedeutet auf englisch leg. Sage Bein auf englisch."; correctAnswer11 = "leg"; break;
		case 11: question11 = "Sonne bedeutet auf englisch sun. Sage Sonne auf englisch."; correctAnswer11 = "sun"; break;
		case 12: question11 = "Immer bedeutet auf englisch always.Sage immer auf englisch"; correctAnswer11 = "always"; break;
		case 13: question11 = "Wasser bedeutet auf englisch water.Sage Wasser auf englisch"; correctAnswer11 = "water"; break;
		case 14: question11 = "Tisch bedeutet auf englisch table. Sage Tisch auf englisch."; correctAnswer11 = "table"; break;
		case 15: question11 = "Stadt bedeutet auf englisch city. Sage Stadt auf englisch."; correctAnswer11 = "city"; break;
		}
	}
	
	private void selectQuestion2() {
		Random r = new Random();
		int questions2 = r.nextInt(18);
		switch(questions2){
		case 1: question2 = "Was bedeutet hallo auf englisch?"; correctAnswer2 = "hello"; break;
		case 2: question2 = "Was bedeutet Baum auf englisch?"; correctAnswer2 = "tree"; break;
		case 3: question2 = "Was bedeutet jetzt auf englisch?"; correctAnswer2 = "now"; break;
		case 4: question2 = "Was bedeutet vielleicht auf englisch?"; correctAnswer2 = "maybe"; break;
		case 5: question2 = "Was bedeutet heute auf englisch?"; correctAnswer2 = "today"; break;
		case 6: question2 = "Was bedeutet Hand auf englisch?"; correctAnswer2 = "hand" ; break;
		case 7: question2 = "Was bedeutet Haare auf englisch?"; correctAnswer2 = "hair"; break;
		case 8: question2 = "Was bedeutet Bein auf englisch?"; correctAnswer2 = "leg"; break;
		case 9: question2 = "Frage?"; correctAnswer2 = "today"; break;
		case 10: question2 = "Frage?"; correctAnswer2 = "today"; break;
		case 11: question2 = "Frage?"; correctAnswer2 = "today"; break;
		case 12: question2 = "Frage?"; correctAnswer2 = "today"; break;
		case 13: question2 = "Frage?"; correctAnswer2 = "today"; break;
		case 14: question2 = "Frage?"; correctAnswer2 = "today"; break;
		case 15: question2 = "Frage?"; correctAnswer2 = "today"; break;
		}
	}
	private void selectQuestion3() {
		Random r = new Random();
		int questions3 = r.nextInt(19);
		switch(questions3){
		case 1: question3 = "Was bedeutet Rad auf englisch?"; correctAnswer3 = "wheel"; break;
		case 2: question3 = "Was bedeutet Bauchnabel auf englisch?"; correctAnswer3 = "belly button"; break;
		case 3: question3 = "Was bedeutet Treppe auf englisch?"; correctAnswer3 = "stairs"; break;
		case 4: question3 = "Was bedeutet Feld auf englisch?"; correctAnswer3 = "field"; break;
		case 5: question3 = "Was heißt gebrochen auf englisch?"; correctAnswer3 = "broken"; break;
		case 6: question3 = "Was heißt Mikrowelle auf englisch?"; correctAnswer3 = "microwave"; break;
		case 7: question3 = "Was heißt aufgeben auf englisch?"; correctAnswer3 = "give up"; break;
		case 8: question3 = "Was heißt Gemeinschaft auf englisch?"; correctAnswer3 = "community"; break;
		case 9: question3 = "Was heißt Sturm auf englisch?"; correctAnswer3 = "gale"; break;
		case 10: question3 = "Frage?"; correctAnswer3 = "today"; break;
		case 11: question3 = "Frage?"; correctAnswer3 = "today"; break;
		case 12: question3 = "Frage?"; correctAnswer3 = "today"; break;
		case 13: question3 = "Frage?"; correctAnswer3 = "today"; break;
		case 14: question3 = "Frage?"; correctAnswer3 = "today"; break;
		case 15: question3 = "Frage?"; correctAnswer3 = "today"; break;
		}
	}
	private void selectQuestion4() {
		Random r = new Random();
		int questions4 = r.nextInt(20);
		switch(questions4){
		case 1: question4 = "Was bedeutet rechthaberisch auf englisch?"; correctAnswer4 = "bossy"; break;
		case 2: question4 = "Was bedeutet Vertreter auf englisch?"; correctAnswer4 = "representative"; break;
		case 3: question4 = "Was bedeutet fließend auf englisch?"; correctAnswer4 = "fluently"; break;
		case 4: question4 = "Was bedeutet launisch auf englisch?"; correctAnswer4 = "moody"; break;
		case 5: question4 = "Was bedeutet Schwiegermutter auf englisch?"; correctAnswer4 = "mother in law"; break;
		case 6: question4 = "Was bedeutet Mittelschicht auf englisch?"; correctAnswer4 = "middle class"; break;
		case 7: question4 = "Frage?"; correctAnswer4 = "today"; break;
		case 8: question4 = "Frage?"; correctAnswer4 = "today"; break;
		case 9: question4 = "Frage?"; correctAnswer4 = "today"; break;
		case 10: question4 = "Frage?"; correctAnswer4 = "today"; break;
		case 11: question4 = "Frage?"; correctAnswer4 = "today"; break;
		case 12: question4 = "Frage?"; correctAnswer4 = "today"; break;
		case 13: question4 = "Frage?"; correctAnswer4 = "today"; break;
		case 14: question4 = "Frage?"; correctAnswer4 = "today"; break;
		case 15: question4 = "Frage?"; correctAnswer4 = "today"; break;
		}
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
		case YesNoVokabelnMedium: resp = evaluateYesNoVokabelnMedium(userRequest); break;
		case YesNoVokabelnHard: resp = evaluateYesNoVokabelnHard(userRequest); break;
		case AnswerVokabelnEasy: resp = evaluateAnswerVokabelnEasy(userRequest); break;
		case AnswerVokabelnMedium: resp = evaluateAnswerVokabelnMedium(userRequest); break;
		case AnswerVokabelnHard: resp = evaluateAnswerVokabelnHard(userRequest); break;
		case AnswerQuizLevelOne: resp = evaluateAnswerQuizLevelOne(userRequest); break;
		case AnswerQuizLevelTwo: resp = evaluateAnswerQuizLevelTwo(userRequest); break;
		case AnswerQuizLevelThree: resp = evaluateAnswerQuizLevelThree(userRequest); break;
		case Menu: resp = evaluateMenu(userRequest); break;
		default: resp = response("Erkannter Text: " + userRequest);
		}   
		return resp;
	}
	
	
	
	/* +++ mein Versuch ein menü state zu erstellen (ist nicht eingebunden) MENU +++ */
	
	private SpeechletResponse evaluateMenu(String userRequest) {

		SsmlOutputSpeech speech = new SsmlOutputSpeech();
		switch(ourUserIntent){ 
		
		default: 
			speech.setSsml("<speak><amazon:effect name=\"whispered\">welcome at quizzitch</amazon:effect></speak>");
			recState = RecognitionState.OneTwo; break; 
		}
		return evaluateYesNo(null);
	}
	
	
	

	
	
	/* Im Vokabelteil: Möchten Sie weitermachen? -> stattdessen Quizzen? */
	private SpeechletResponse evaluateYesNoVokabelnEasy(String userRequest) {	
		SpeechletResponse res = null;
		recognizeUserIntent(userRequest);
		switch (ourUserIntent) {
		case resume: {
			selectQuestion();
			res = askUserResponse(question);
			recState = RecognitionState.AnswerVokabelnEasy; break;
		} case yess: {
			selectQuestion();
			res = askUserResponse(question);
			recState = RecognitionState.AnswerVokabelnEasy; break;
			
		} case quit: {
			res = responseWithFlavour(goodbyeMsg, 6); break;
		} case no: {
			res = responseWithFlavour(goodbyeMsg, 6); break;
			
		} case menu: {
			res = askUserResponse(welcomeMsg);
			// res = responseWithFlavour(welcomeMsg, 5);
			recState = RecognitionState.OneTwo; break;
			
		} case difficulty: {
			res = askUserResponse(difficultyMsg);
			recState = RecognitionState.Vokabel; break;
		} default: {
			res = askUserResponse(errorYesNoMsg);
		}
		}
		return res;
	}
	
	private SpeechletResponse evaluateYesNoVokabelnMedium(String userRequest) {	
		SpeechletResponse res = null;
		recognizeUserIntent(userRequest);
		switch (ourUserIntent) {
		case resume: {
			selectQuestion1();
			res = askUserResponse(question1);
			recState = RecognitionState.AnswerVokabelnMedium; break;
			
		} case yess: {
			selectQuestion1();
			res = askUserResponse(question1);
			recState = RecognitionState.AnswerVokabelnMedium; break; 
			
		} case quit: {
			res = responseWithFlavour(goodbyeMsg, 6); break;
			
		} case no: {
			res = responseWithFlavour(goodbyeMsg, 6); break;
			
		} case menu: {
			res = askUserResponse(welcomeMsg);
			recState = RecognitionState.OneTwo; break;
		} case difficulty: {
			res = askUserResponse(difficultyMsg);
			recState = RecognitionState.Vokabel; break;
		} default: {
			res = askUserResponse(errorYesNoMsg);
		}
		}
		return res;
	}
	
	private SpeechletResponse evaluateYesNoVokabelnHard(String userRequest) {	
		SpeechletResponse res = null;
		recognizeUserIntent(userRequest);
		switch (ourUserIntent) {
		case resume: {
			selectQuestion11();
			res = askUserResponse(question11);
			recState = RecognitionState.AnswerVokabelnHard; break;
		} case yess: {
			selectQuestion11();
			res = askUserResponse(question11);
			recState = RecognitionState.AnswerVokabelnHard; break;
			
		} case quit: {
			res = responseWithFlavour(goodbyeMsg, 6); break;
		} case no: {
			res = responseWithFlavour(goodbyeMsg, 6); break;
			
		} case menu: {
			res = askUserResponse(welcomeMsg);
			// speech.setSsml("<speak><amazon:effect name=\"whispered\">test das ist</amazon:effect></speak>");
			recState = RecognitionState.OneTwo; break;
		} case difficulty: {
			res = askUserResponse(difficultyMsg);
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
			selectQuestion2();
			res = askUserResponse(question2);
			recState = RecognitionState.AnswerQuizLevelOne; break;
		} case yess: {
			selectQuestion2();
			res = askUserResponse(question2);
			recState = RecognitionState.AnswerQuizLevelOne; break;
			
		} case quit: {
			res = responseWithFlavour(goodbyeMsg, 6); break;
		} case no: {
			res = responseWithFlavour(goodbyeMsg, 6); break;
		} case menu: {
			res = askUserResponse(welcomeMsg);
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
			selectQuestion3();
			res = askUserResponse(question3);
			recState = RecognitionState.AnswerQuizLevelTwo; break;
		} case yess: {
			selectQuestion3();
			res = askUserResponse(question3);
			recState = RecognitionState.AnswerQuizLevelTwo; break;
			
		} case quit: {
			res = responseWithFlavour(goodbyeMsg, 6); break;
		} case no: {
			res = responseWithFlavour(goodbyeMsg, 6); break;
			
		} case menu: {
			res = askUserResponse(welcomeMsg);
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
			selectQuestion4();
			res = askUserResponse(question4);
			recState = RecognitionState.AnswerQuizLevelThree; break;
		} case yess: {
			selectQuestion4();
			res = askUserResponse(question4);
			recState = RecognitionState.AnswerQuizLevelThree; break;
			
		} case quit: {
			res = responseWithFlavour(goodbyeMsg, 6); break;
		} case no: {
			res = responseWithFlavour(goodbyeMsg, 6); break;
			
		} case menu: {
			res = askUserResponse(welcomeMsg);
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
			selectQuestion4();
			res = askUserResponse(question4);
			recState = RecognitionState.AnswerQuizLevelThree; break;
		} case yess: {
			selectQuestion4();
			res = askUserResponse(question4);
			recState = RecognitionState.AnswerQuizLevelThree; break;
			
		} case quit: {
			res = responseWithFlavour(goodbyeMsg, 6); break;
		} case no: {
			res = responseWithFlavour(goodbyeMsg, 6); break;
		} case levelone: {
			res = askUserResponse(question2);
			recState = RecognitionState.AnswerQuizLevelOne; break;
		} case leveltwo: {
			res = askUserResponse(question3);
			recState = RecognitionState.AnswerQuizLevelTwo; break;
			
		} case menu: {
			res = askUserResponse(welcomeMsg);
			recState = RecognitionState.OneTwo; break;
			
		} default: {
			res = askUserResponse(errorYesNoMsg);
		}
		}
		return res;
	}

	
	/*Weiter spielen oder aufhören?*/
	private SpeechletResponse evaluateYesNo(String userRequest) {
		SpeechletResponse res = null;
		recognizeUserIntent(userRequest);
		switch (ourUserIntent) {
		case resume: {
			selectQuestion();
			res = askUserResponse(question);
			recState = RecognitionState.Answer; break;
		} case quit: {
			res = responseWithFlavour(goodbyeMsg, 6); break;
		} case menu: {
			res = askUserResponse(welcomeMsg);
			recState = RecognitionState.OneTwo; break;
		} default: {
			res = askUserResponse(errorYesNoMsg);
		}
		}
		return res;
	}
	
	/*Weiter spielen oder aufhören, Level?*/
	private SpeechletResponse evaluateYesNoTwo(String userRequest) {
		SpeechletResponse res = null;
		recognizeUserIntent(userRequest);
		switch (ourUserIntent) {
		case resume: {
			selectQuestion2();
			res = askUserResponse(question2);
			recState = RecognitionState.WhichPlayer; break;
		} case quit: {
			res = responseWithFlavour(goodbyeMsg, 6); break;
		} case menu: {
			res = askUserResponse(welcomeMsg);
			recState = RecognitionState.OneTwo; break;
		} default: {
			res = askUserResponse(errorYesNoMsg);
		}
		}
		return res;
	}
	
	/*Weiter spielen oder aufhören, Level?*/
	private SpeechletResponse evaluateYesNoLevel(String userRequest) {
		SpeechletResponse res = null;
		recognizeUserIntent(userRequest);
		switch (ourUserIntent) {
		case resume: {
			selectQuestion3();
			res = askUserResponse(question3);
			recState = RecognitionState.WhichPlayerThree; break;
		} case quit: {
			res = responseWithFlavour(goodbyeMsg, 6); break;
		} case menu: {
			res = askUserResponse(welcomeMsg);
			recState = RecognitionState.OneTwo; break;
		} default: {
			res = askUserResponse(errorYesNoMsg);
		}
		}
		return res;
	}
	
	/*Weiter spielen oder aufhören, Level?*/
	private SpeechletResponse evaluateYesNoLevelTwo(String userRequest) {
		SpeechletResponse res = null;
		recognizeUserIntent(userRequest);
		switch (ourUserIntent) {
		case resume: {
			selectQuestion4();
			res = askUserResponse(question4);
			recState = RecognitionState.WhichPlayerFour; break;
		} case quit: {
			res = responseWithFlavour(goodbyeMsg, 6); break;
		} case menu: {
			res = askUserResponse(welcomeMsg);
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
			questions = 1;
			questions2 = 1;
			questions3 = 1;
			questions4 = 1;
			selectQuestion();
			res = askUserResponse(question);
			recState = RecognitionState.WhichPlayer; break;
		} case quit: {
			res = responseWithFlavour(goodbyeMsg, 6); break;
		} case menu: {
			res = askUserResponse(welcomeMsg);
			sum = 0;
			sum2 = 0;
			sum3 = 0;
			sum4 = 0;
			sum5 = 0;
			sum6 = 0;
			sum7 = 0;
			questions = 1;
			questions2 = 1;
			questions3 = 1;
			questions4 = 1;
			recState = RecognitionState.OneTwo; break;
		} default: {
			res = askUserResponse(errorAgainOrMenuMsg);
		}
		}
		return res;
	}
	
	/* Ein oder zwei Spieler? */

	private SpeechletResponse evaluateOneTwo(String userRequest) {
		SpeechletResponse res = null;
		recognizeUserIntent(userRequest);
		switch (ourUserIntent) {
		
		case onne: {
			res = askUserResponse(singleMsg);
			recState = RecognitionState.VokabelQuiz; break;
			
		}  case menu: {
			res = askUserResponse(welcomeMsg);
			recState = RecognitionState.OneTwo; break;
		} case twwo: {
			selectQuestion2();
			res = askUserResponse(multiMsg+" "+question2);
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
			res = askUserResponse(difficultyMsg);
			recState = RecognitionState.Vokabel; break;
		} case yess: {
			res = askUserResponse(difficultyMsg);
			recState = RecognitionState.Vokabel; break;
		} case quiz: {
			selectQuestion2();
			res = askUserResponse(singleQuizMsg+" "+question2);
			recState = RecognitionState.AnswerQuizLevelOne; break;
		} case menu: {
			res = askUserResponse(welcomeMsg);
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
			res = askUserResponse(question);
			recState = RecognitionState.AnswerVokabelnEasy; break;
		} case medium: {
			selectQuestion1();
			res = askUserResponse(question1);
			recState = RecognitionState.AnswerVokabelnMedium; break;
		} case hard: {
			selectQuestion11();
			res = askUserResponse(question11);
			recState = RecognitionState.AnswerVokabelnHard; break;
		} case menu: {
			res = askUserResponse(welcomeMsg);
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
			selectQuestion2();
			res = askUserResponse(question2);
			recState = RecognitionState.AnswerQuiz; break;
		} 
		
		case no: {
			res = responseWithFlavour(goodbyeMsg, 6); break;
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
			res = askUserResponse(SpielerEinsKurzMsg);
			recState = RecognitionState.AnswerTwo;
		}; break;
		case onne: {
			res = askUserResponse(SpielerEinsKurzMsg);
			recState = RecognitionState.AnswerTwo;
		}; break;
		case playertwo: {
			res = askUserResponse(SpielerZweiKurzMsg);
			recState = RecognitionState.AnswerThree;
		}; break;
		 case twwo: {
			res = askUserResponse(SpielerZweiKurzMsg);
			recState = RecognitionState.AnswerThree;
		}; break;
		case menu: {
			res = askUserResponse(welcomeMsg);
			recState = RecognitionState.OneTwo;
		}; break;
		default: {
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
		case onne: {
			res = askUserResponse(SpielerEinsKurzMsg);
			recState = RecognitionState.AnswerFour; break;
		} case twwo: {
			res = askUserResponse(SpielerZweiKurzMsg);
			recState = RecognitionState.AnswerFive; break;
		} case menu: {
			res = askUserResponse(welcomeMsg);
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
		case onne: {
			res = askUserResponse(SpielerEinsKurzMsg);
			recState = RecognitionState.AnswerSix; break;
		} case twwo: {
			res = askUserResponse(SpielerZweiKurzMsg);
			recState = RecognitionState.AnswerSeven; break;
		} case menu: {
			res = askUserResponse(welcomeMsg);
			recState = RecognitionState.OneTwo; break;
		} default: {
			res = askUserResponse(errorSpielereinszweiMsg);
		}
		}
		return res;
	}
	
	/*in den Vokabeln*/
	private SpeechletResponse evaluateAnswerVokabelnEasy(String userRequest) {
		SpeechletResponse res = null;
		recognizeUserIntent(userRequest);{
				logger.info("User answer ="+ ourUserIntent.name().toLowerCase()+ "/correct answer="+correctAnswer);
				if (ourUserIntent.name().toLowerCase().equals(correctAnswer)) {
					logger.info("User answer recognized as correct.");
					if (sum == 100) {
						res = responseWithFlavour(correctMsg+" "+congratsMsg+" "+goodbyeMsg,6);
					} else {
						recState = RecognitionState.YesNoVokabelnEasy;
						res = askUserResponse(correctMsg/*+" "+buildString(sumMsg, String.valueOf(sum), " ")*/+" "+resume1Msg);
						/*recState = RecognitionState.YesNo;*/
					}
				} else {
					/*increaseQuestions();*/
					recState = RecognitionState.YesNoVokabelnEasy;
					res = askUserResponse(wrongMsg/*+" "+buildString(sumMsg, String.valueOf(sum), " ")*/+" "+resume1Msg);
				}
			} /*else {
				res = askUserResponse(errorAnswerMsg);
			}
		/*}
		}*/
		return res;
	}
	
	/*in den Vokabeln*/
	private SpeechletResponse evaluateAnswerVokabelnMedium(String userRequest) {
		SpeechletResponse res = null;
		recognizeUserIntent(userRequest);{
				logger.info("User answer ="+ ourUserIntent.name().toLowerCase()+ "/correct answer="+correctAnswer1);
				if (ourUserIntent.name().toLowerCase().equals(correctAnswer1)) {
					logger.info("User answer recognized as correct.");
					if (sum2 == 1000000) {
						res = responseWithFlavour(correctMsg+" "+congratsMsg+" "+goodbyeMsg,6);
					} else {
						recState = RecognitionState.YesNoVokabelnMedium;
						res = askUserResponse(correctMsg/*+" "+buildString(sumMsg, String.valueOf(sum), " ")*/+" "+resume1Msg);
						/*recState = RecognitionState.YesNo;*/
					}
				} else {
					/*increaseQuestions1();*/
					recState = RecognitionState.YesNoVokabelnMedium;
					res = askUserResponse(wrongMsg+" "+resume1Msg);
				}
			} /*else {
				res = askUserResponse(errorAnswerMsg);
			}
		/*}
		}*/
		return res;
	}
	
	/*in den Vokabeln*/
	private SpeechletResponse evaluateAnswerVokabelnHard(String userRequest) {
		SpeechletResponse res = null;
		recognizeUserIntent(userRequest);{
				logger.info("User answer ="+ ourUserIntent.name().toLowerCase()+ "/correct answer="+correctAnswer11);
				if (ourUserIntent.name().toLowerCase().equals(correctAnswer11)) {
					logger.info("User answer recognized as correct.");
					/*increaseSum();*/
					/*increaseQuestions11();*/
					if (sum3 == 1000000) {
						res = responseWithFlavour(correctMsg+" "+congratsMsg+" "+goodbyeMsg,6);
					} else {
						recState = RecognitionState.YesNoVokabelnHard;
						res = askUserResponse(correctMsg/*+" "+buildString(sumMsg, String.valueOf(sum), " ")*/+" "+resume1Msg);
						/*recState = RecognitionState.YesNo;*/
					}
				} else {
					/*increaseQuestions11();*/
					recState = RecognitionState.YesNoVokabelnHard;
					res = askUserResponse(wrongMsg/*+" "+buildString(sumMsg, String.valueOf(sum), " ")*/+" "+resume1Msg);
				}
			} /*else {
				res = askUserResponse(errorAnswerMsg);
			}
		/*}
		}*/
		return res;
	}

	
	/*im Quiz Level 1*/
	private SpeechletResponse evaluateAnswerQuizLevelOne(String userRequest) {
		SpeechletResponse res = null;
		recognizeUserIntent(userRequest);{
				logger.info("User answer ="+ ourUserIntent.name().toLowerCase()+ "/correct answer="+correctAnswer2);
				if (ourUserIntent.name().toLowerCase().equals(correctAnswer2)) {
					logger.info("User answer recognized as correct.");
					increaseSum();
					/*increaseQuestions2();*/
					if (sum >= 50) {
						recState = RecognitionState.YesNoQuizLevelTwo;
						res = askUserResponse(correctMsg+" "+buildString(sumMsg, String.valueOf(sum), " ")+" "+resumeEinzelQuizLevelTwoMsg);
					} else {
						selectQuestion2();
						recState = RecognitionState.AnswerQuizLevelOne;
						res = askUserResponse(correctMsg+" "+buildString(sumMsg, String.valueOf(sum), " ")+" "+"Next question:"+" "+question2/*+" "+resume1Msg*/);
						/*recState = RecognitionState.YesNo;*/
					}
				} else {
					/*increaseQuestions2();*/
					selectQuestion2();
					recState = RecognitionState.AnswerQuizLevelOne;
					res = askUserResponse(wrongMsg+" "+buildString(sumMsg, String.valueOf(sum), " ")+" "+"Next question:"+" "+question2/*+" "+resume1Msg*/);
					/*recState = RecognitionState.YesNo;*/
				}
			} /*else {
				res = askUserResponse(errorAnswerMsg);
			}
		}
		}*/
		return res;
	}
	
	/*im Quiz Level 2*/
	private SpeechletResponse evaluateAnswerQuizLevelTwo(String userRequest) {
		SpeechletResponse res = null;
		recognizeUserIntent(userRequest);{
				logger.info("User answer ="+ ourUserIntent.name().toLowerCase()+ "/correct answer="+correctAnswer3);
				if (ourUserIntent.name().toLowerCase().equals(correctAnswer3)) {
					logger.info("User answer recognized as correct.");
					increaseSum();
					/*increaseQuestions3();*/
					if (sum >= 50) {
						recState = RecognitionState.YesNoQuizLevelThree;
						res = askUserResponse(correctMsg+" "+buildString(sumMsg, String.valueOf(sum), " ")+" "+resumeEinzelQuizLevelThreeMsg);
					} else {
						
						selectQuestion2();
						recState = RecognitionState.AnswerQuizLevelTwo;
						res = askUserResponse(correctMsg+" "+buildString(sumMsg, String.valueOf(sum), " ")+question3);
						
						/*recState = RecognitionState.YesNo;*/
					}
				} else {
					/*increaseQuestions3();*/
					selectQuestion2();
					recState = RecognitionState.AnswerQuizLevelTwo;
					res = askUserResponse(wrongMsg+" "+buildString(sumMsg, String.valueOf(sum), " ")+" "+question3);
				}
			} /*else {
				res = askUserResponse(errorAnswerMsg);
			}
		/*}
		}*/
		return res;
	}
	
	/*im Quiz Level 3*/
	private SpeechletResponse evaluateAnswerQuizLevelThree(String userRequest) {
		SpeechletResponse res = null;
		recognizeUserIntent(userRequest);{
				logger.info("User answer ="+ ourUserIntent.name().toLowerCase()+ "/correct answer="+correctAnswer4);
				if (ourUserIntent.name().toLowerCase().equals(correctAnswer4)) {
					logger.info("User answer recognized as correct.");
					increaseSum();
					/*increaseQuestions4();*/
					if (sum >= 50) {
						recState = RecognitionState.YesNoQuizLevelEnd;
						res = askUserResponse(correctMsg+" "+buildString(sumMsg, String.valueOf(sum), " ")+" "+resumeEinzelQuizEndMsg);
					} else {
						recState = RecognitionState.AnswerQuizLevelThree;
						res = askUserResponse(correctMsg+" "+buildString(sumMsg, String.valueOf(sum), " ")+" "+question4);
						/*recState = RecognitionState.YesNo;*/
					}
				} else {
					/*increaseQuestions4();*/
					recState = RecognitionState.AnswerQuizLevelThree;
					res = askUserResponse(wrongMsg+" "+buildString(sumMsg, String.valueOf(sum), " ")+" "+question4);
				}
			} /*else {
				res = askUserResponse(errorAnswerMsg);
			}
		/*}
		}*/
		return res;
	}
	
	
	
	
	
	
	
	/*Mehrspielermodus: Antwort von Spieler eins in Level eins*/
	private SpeechletResponse evaluateAnswerTwo(String userRequest) {
		SpeechletResponse res = null;
		recognizeUserIntent(userRequest);{
				logger.info("User answer ="+ ourUserIntent.name().toLowerCase()+ "/correct answer="+correctAnswer2);
				
				if (ourUserIntent.name().toLowerCase().equals(correctAnswer2)) {
					logger.info("User answer recognized as correct.");
					increaseSum2();
					/*increaseQuestions2();*/
					if (sum2 == 40) {
						recState = RecognitionState.YesNoLevel;
						res = askUserResponse(correctMsg+" "+buildString2(sumTwoMsg, String.valueOf(sum2), " ")+" "+buildString3(sumThreeMsg, String.valueOf(sum3), " ")+" "+playerOneWins+" "+resumeLevelMsg);
					} else {
						recState = RecognitionState.YesNoTwo;
						res = askUserResponse(correctMsg+" "+buildString2(sumTwoMsg, String.valueOf(sum2), " ")+" "+buildString3(sumThreeMsg, String.valueOf(sum3), " ")+" "+resume1Msg);
						/*recState = RecognitionState.YesNo;*/
					}
				} else {
					/*increaseQuestions2();*/
					recState = RecognitionState.YesNoTwo;
					res = askUserResponse(wrongMsg+" "+buildString2(sumTwoMsg, String.valueOf(sum2), " ")+" "+buildString3(sumThreeMsg, String.valueOf(sum3), " ")+" "+resume1Msg);
				}
			} /*else {
				res = askUserResponse(errorAnswerMsg);
			}
		/*}
		}*/
				return res;
	}
	
	/*Mehrspielermodus: Antwort von Spieler eins in Level zwei*/
	private SpeechletResponse evaluateAnswerFour(String userRequest) {
		SpeechletResponse res = null;
		recognizeUserIntent(userRequest);{
				logger.info("User answer ="+ ourUserIntent.name().toLowerCase()+ "/correct answer="+correctAnswer3);
				if (ourUserIntent.name().toLowerCase().equals(correctAnswer3)) {
					logger.info("User answer recognized as correct.");
					increaseSum4();
					/*increaseQuestions3();*/
					if (sum4 >= 40) {
						recState = RecognitionState.YesNoLevelTwo;
						res = askUserResponse(correctMsg+" "+buildString2(sumTwoMsg, String.valueOf(sum2+sum4), " ")+" "+buildString3(sumThreeMsg, String.valueOf(sum3+sum5), " ")+" "+playerOneWins+" "+resumeLevelTwoMsg);
					} else {
						recState = RecognitionState.YesNoLevel;
						res = askUserResponse(correctMsg+" "+buildString2(sumTwoMsg, String.valueOf(sum2+sum4), " ")+" "+buildString3(sumThreeMsg, String.valueOf(sum3+sum5), " ")+" "+resume1Msg);
						/*recState = RecognitionState.YesNo;*/
					}
				} else {
					decreaseSum4();
					increaseSum5();
					/*increaseQuestions3();*/
					recState = RecognitionState.YesNoLevel;
					res = askUserResponse(wrongMsg+" "+buildString2(sumTwoMsg, String.valueOf(sum2+sum4), " ")+" "+buildString3(sumThreeMsg, String.valueOf(sum3+sum5), " ")+" "+resume1Msg);
				}
			} /*else {
				res = askUserResponse(errorAnswerMsg);
			}
		/*}
		}*/
				return res;
	}
	
	/*Mehrspielermodus: Antwort von Spieler eins in Level drei*/
	private SpeechletResponse evaluateAnswerSix(String userRequest) {
		SpeechletResponse res = null;
		recognizeUserIntent(userRequest);{
				logger.info("User answer ="+ ourUserIntent.name().toLowerCase()+ "/correct answer="+correctAnswer4);
				if (ourUserIntent.name().toLowerCase().equals(correctAnswer4)) {
					logger.info("User answer recognized as correct.");
					increaseSum6();
					/*increaseQuestions4();*/
					if (sum2+sum4+sum6>sum3+sum5+sum7 & sum6 >= 40) {
						recState = RecognitionState.AgainOrMenu;
						res = askUserResponse(correctMsg+" "+buildString2(sumTwoMsg, String.valueOf(sum2+sum4+sum6), " ")+" "+buildString3(sumThreeMsg, String.valueOf(sum3+sum5+sum7), " ")+" "+playerOneWinsGame+" "+againOrMenuMsg);
					} else {
						recState = RecognitionState.YesNoLevelTwo;
						res = askUserResponse(correctMsg+" "+buildString2(sumTwoMsg, String.valueOf(sum2+sum4+sum6), " ")+" "+buildString3(sumThreeMsg, String.valueOf(sum3+sum5+sum7), " ")+" "+resume1Msg);
						/*recState = RecognitionState.YesNo;*/
					}
				} else {
					/*increaseQuestions4();*/
					recState = RecognitionState.YesNoLevelTwo;
					res = askUserResponse(wrongMsg+" "+buildString2(sumTwoMsg, String.valueOf(sum2+sum4+sum6), " ")+" "+buildString3(sumThreeMsg, String.valueOf(sum3+sum5+sum7), " ")+" "+resume1Msg);
				}
			} /*else {
				res = askUserResponse(errorAnswerMsg);
			}
		/*}
		}*/
				return res;
	}
	
	/*Mehrspielermodus: Antwort von Spieler zwei in Level eins*/
	private SpeechletResponse evaluateAnswerThree(String userRequest) {
		SpeechletResponse res = null;
		recognizeUserIntent(userRequest);{
				logger.info("User answer ="+ ourUserIntent.name().toLowerCase()+ "/correct answer="+correctAnswer2);
				
				if (ourUserIntent.name().toLowerCase().equals(correctAnswer2)) {
					logger.info("User answer recognized as correct.");
					increaseSum3();
					/*increaseQuestions2();*/
					if (sum3 >= 40) {
						/*res = response(correctMsg+" "+congratsMsg+" "+goodbyeMsg);*/
						recState = RecognitionState.YesNoLevel;
						res = askUserResponse(correctMsg+" "+buildString2(sumTwoMsg, String.valueOf(sum2), " ")+" "+buildString3(sumThreeMsg, String.valueOf(sum3), " ")+" "+playerTwoWins+" "+resumeLevelMsg);
					} else {
						recState = RecognitionState.YesNoTwo;
						res = askUserResponse(correctMsg+" "+buildString2(sumTwoMsg, String.valueOf(sum2), " ")+" "+buildString3(sumThreeMsg, String.valueOf(sum3), " ")+" "+resume1Msg);
						/*recState = RecognitionState.YesNo;*/
					}
				} else {
					/*increaseQuestions2();*/
					recState = RecognitionState.YesNoTwo;
					res = askUserResponse(wrongMsg+" "+buildString2(sumTwoMsg, String.valueOf(sum2), " ")+" "+buildString3(sumThreeMsg, String.valueOf(sum3), " ")+" "+resume1Msg);
				}
			} /*else {
				res = askUserResponse(errorAnswerMsg);
			}
		/*}
		}*/
				return res;
	}
	
	/*Mehrspielermodus: Antwort von Spieler zwei in Level zwei*/
	private SpeechletResponse evaluateAnswerFive(String userRequest) {
		SpeechletResponse res = null;
		recognizeUserIntent(userRequest);{
				logger.info("User answer ="+ ourUserIntent.name().toLowerCase()+ "/correct answer="+correctAnswer3);
				if (ourUserIntent.name().toLowerCase().equals(correctAnswer3)) {
					logger.info("User answer recognized as correct.");
					increaseSum5();
					/*increaseQuestions3();*/
					if (sum5 >= 40) {
						/*res = response(correctMsg+" "+congratsMsg+" "+goodbyeMsg);*/
						recState = RecognitionState.YesNoLevelTwo;
						res = askUserResponse(correctMsg+" "+buildString2(sumTwoMsg, String.valueOf(sum2+sum4), " ")+" "+buildString3(sumThreeMsg, String.valueOf(sum3+sum5), " ")+" "+playerTwoWins+" "+resumeLevelTwoMsg);
					} else {
						recState = RecognitionState.YesNoLevel;
						res = askUserResponse(correctMsg+" "+buildString2(sumTwoMsg, String.valueOf(sum2+sum4), " ")+" "+buildString3(sumThreeMsg, String.valueOf(sum3+sum5), " ")+" "+resume1Msg);
						/*recState = RecognitionState.YesNo;*/
					}
				} else {
					decreaseSum5();
					increaseSum4();
					/*increaseQuestions3();*/
					recState = RecognitionState.YesNoLevel;
					res = askUserResponse(wrongMsg+" "+buildString2(sumTwoMsg, String.valueOf(sum2+sum4), " ")+" "+buildString3(sumThreeMsg, String.valueOf(sum3+sum5), " ")+" "+resume1Msg);
				}
			} /*else {
				res = askUserResponse(errorAnswerMsg);
			}
		/*}
		}*/
				return res;
	}
	
	/*Mehrspielermodus: Antwort von Spieler zwei in Level drei*/
	private SpeechletResponse evaluateAnswerSeven(String userRequest) {
		SpeechletResponse res = null;
		recognizeUserIntent(userRequest);{
				logger.info("User answer ="+ ourUserIntent.name().toLowerCase()+ "/correct answer="+correctAnswer4);
				if (ourUserIntent.name().toLowerCase().equals(correctAnswer4)) {
					logger.info("User answer recognized as correct.");
					increaseSum7();
					/*increaseQuestions4();*/
					if (sum3+sum5+sum7>sum2+sum4+sum6 & sum7 >= 40) {
						recState = RecognitionState.AgainOrMenu;
						res = askUserResponse(correctMsg+" "+buildString2(sumTwoMsg, String.valueOf(sum2+sum4+sum6), " ")+" "+buildString3(sumThreeMsg, String.valueOf(sum3+sum5+sum7), " ")+" "+playerTwoWinsGame+" "+againOrMenuMsg);
					} else {
						recState = RecognitionState.YesNoLevelTwo;
						res = askUserResponse(correctMsg+" "+buildString2(sumTwoMsg, String.valueOf(sum2+sum4+sum6), " ")+" "+buildString3(sumThreeMsg, String.valueOf(sum3+sum5+sum7), " ")+" "+resume1Msg);
						/*recState = RecognitionState.YesNo;*/
					}
				} else {
					/*increaseQuestions4();*/
					recState = RecognitionState.YesNoLevelTwo;
					res = askUserResponse(wrongMsg+" "+buildString2(sumTwoMsg, String.valueOf(sum2+sum4+sum6), " ")+" "+buildString3(sumThreeMsg, String.valueOf(sum3+sum5+sum7), " ")+" "+resume1Msg);
				}
			} /*else {
				res = askUserResponse(errorAnswerMsg);
			}
		/*}
		}*/
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
		
		
		String pattern4 = "(I think )?(the answer is )?(\\bnow\\b)";
		String pattern5 = "\\btree\\b";
		String pattern6 = "\\bhello\\b";
		String pattern7 = "(I would like to )?(I want to )?(\\bresume\\b)";
		String pattern8 = "(I would like to )?(I want to )?(\\bleave\\b)";
		String pattern9 = "\\bmaybe\\b";
		String pattern10 = "\\btoday\\b";
		String pattern11 = "\\bonne\\b";
		String pattern12 = "\\btwo\\b";
		String pattern13 = "\\bvocabulary\\b";
		String pattern14 = "(I would like to )?(do )?(play )?(a )?(I want to )?(\\bquiz\\b)";
		String pattern15 = "\\beasy\\b";
		String pattern16 = "\\bmedium\\b";
		String pattern17 = "\\bhard\\b";
		String pattern18 = "(player )?(\\bone\\b)";
		/* String pattern19 = "\\bplayer\\stwo\\b"; */
		
		String pattern19 = "(ich nehme )?(den )?(fiftyfifty|fünfzigfünfzig) joker( bitte)?";
		
		String pattern20 = "\\bbye\\b";
		String pattern21 = "\\bmenu\\b";
		String pattern22 = "\\bmoin\\b";
		String pattern23 = "\\bnext\\slevel\\b";
		String pattern24 = "\\bbanana\\b";
		String pattern25 = "\\bagain\\b";
		String pattern26 = "\\blight\\b";
		String pattern27 = "\\bbone\\b";
		String pattern28 = "\\btwwo\\b";
		String pattern29 = "\\byes\\b";
		String pattern30 = "\\bno\\b";
		String pattern31 = "\\bquiz\\b";
		String pattern32 = "\\bquit\\b";
		String pattern33 = "\\bfood\\b";
		String pattern34 = "\\bhead\\b";
		String pattern35 = "\\bhair\\b";
		String pattern36 = "\\bleg\\b";
		String pattern37 = "\\bsun\\b";
		String pattern38 = "\\balways\\b";
		String pattern39 = "\\bwater\\b";
		String pattern40 = "\\btable\\b";
		String pattern41 = "\\bcity\\b";
		String pattern42 = "\\bstairs\\b";
		String pattern43 = "\\bhaircolour\\b";
		String pattern44 = "\\bwheel\\b";
		String pattern45 = "\\bbelly\\sbutton\\b";
		String pattern46 = "\\bbroken\\b";
		String pattern47 = "\\bcontract\\b";
		String pattern48 = "\\bcommunity\\b";
		String pattern49 = "\\bcandle\\b";
		String pattern50 = "\\bfield\\b";
		String pattern51 = "\\bgale\\b";
		String pattern52 = "\\bgive\\sup\\b";
		String pattern53 = "\\bmicrowave\\b";
		String pattern54 = "\\bpillow\\b";
		String pattern55 = "\\bpolicy\\b";
		String pattern56 = "\\bbalance\\b";
		String pattern57 = "\\bacquaintance\\b";
		String pattern58 = "\\bbossy\\b";
		String pattern59 = "\\bconfident\\b";
		String pattern60 = "\\bgenerous\\b";
		String pattern61 = "\\bmiddleclass\\b";
		String pattern62 = "\\bmother\\sin\\slaw\\b";
		String pattern63 = "\\bmoody\\b";
		String pattern64 = "\\breliable\\b";
		String pattern65 = "\\baccountancy\\b";
		String pattern66 = "\\bapply\\b";
		String pattern67 = "\\bfluently\\b";
		String pattern68 = "\\binsist\\b";
		String pattern69 = "\\brepresentative\\b";
		String pattern70 = "\\bsmoothly\\b";
		String pattern71 = "\\bbe\\swilling\\sto\\b";
		String pattern72 = "\\bdifficulty\\b";
		String pattern73 = "(I would like to )?(I want to )?(train )?(improve my )?(\\bvocab\\b)(skills )?";
		String pattern74 = "\\byess\\b";
		String pattern75 = "\\bhey\\b";
		String pattern76 = "\\bhand\\b";
		/*String pattern77 = "\\bbutton\\b";*/
		
		
		
		

		Pattern p4 = Pattern.compile(pattern4);
		Matcher m4 = p4.matcher(userRequest);
		Pattern p5 = Pattern.compile(pattern5);
		Matcher m5 = p5.matcher(userRequest);
		Pattern p6 = Pattern.compile(pattern6);
		Matcher m6 = p6.matcher(userRequest);
		Pattern p7 = Pattern.compile(pattern7);
		Matcher m7 = p7.matcher(userRequest);
		Pattern p8 = Pattern.compile(pattern8);
		Matcher m8 = p8.matcher(userRequest);
		Pattern p9 = Pattern.compile(pattern9);
		Matcher m9 = p9.matcher(userRequest);
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
		Matcher m17= p17.matcher(userRequest);
		Pattern p18 = Pattern.compile(pattern18);
		Matcher m18= p18.matcher(userRequest);
		Pattern p19 = Pattern.compile(pattern19);
		Matcher m19= p19.matcher(userRequest);
		Pattern p20 = Pattern.compile(pattern20);
		Matcher m20= p20.matcher(userRequest);
		Pattern p21 = Pattern.compile(pattern21);
		Matcher m21= p21.matcher(userRequest);
		Pattern p22 = Pattern.compile(pattern22);
		Matcher m22= p22.matcher(userRequest);
		Pattern p23 = Pattern.compile(pattern23);
		Matcher m23= p23.matcher(userRequest);
		Pattern p24 = Pattern.compile(pattern24);
		Matcher m24= p24.matcher(userRequest);
		Pattern p25 = Pattern.compile(pattern25);
		Matcher m25= p25.matcher(userRequest);
		Pattern p26 = Pattern.compile(pattern26);
		Matcher m26= p26.matcher(userRequest);
		Pattern p27 = Pattern.compile(pattern27);
		Matcher m27= p27.matcher(userRequest);
		Pattern p28 = Pattern.compile(pattern28);
		Matcher m28= p28.matcher(userRequest);
		Pattern p29 = Pattern.compile(pattern29);
		Matcher m29= p29.matcher(userRequest);
		Pattern p30 = Pattern.compile(pattern30);
		Matcher m30= p30.matcher(userRequest);
		Pattern p31 = Pattern.compile(pattern31);
		Matcher m31= p31.matcher(userRequest);
		Pattern p32 = Pattern.compile(pattern32);
		Matcher m32= p32.matcher(userRequest);
		Pattern p33 = Pattern.compile(pattern33);
		Matcher m33= p33.matcher(userRequest);
		Pattern p34 = Pattern.compile(pattern34);
		Matcher m34= p34.matcher(userRequest);
		Pattern p35 = Pattern.compile(pattern35);
		Matcher m35= p35.matcher(userRequest);
		Pattern p36 = Pattern.compile(pattern36);
		Matcher m36= p36.matcher(userRequest);
		Pattern p37 = Pattern.compile(pattern37);
		Matcher m37 = p37.matcher(userRequest);
		Pattern p38 = Pattern.compile(pattern38);
		Matcher m38 = p38.matcher(userRequest);
		Pattern p39 = Pattern.compile(pattern39);
		Matcher m39 = p39.matcher(userRequest);
		Pattern p40 = Pattern.compile(pattern40);
		Matcher m40 = p40.matcher(userRequest);
		Pattern p41 = Pattern.compile(pattern41);
		Matcher m41 = p41.matcher(userRequest);
		Pattern p42 = Pattern.compile(pattern42);
		Matcher m42 = p42.matcher(userRequest);
		Pattern p43 = Pattern.compile(pattern43);
		Matcher m43= p43.matcher(userRequest);
		Pattern p44 = Pattern.compile(pattern44);
		Matcher m44= p44.matcher(userRequest);
		Pattern p45 = Pattern.compile(pattern45);
		Matcher m45= p45.matcher(userRequest);
		Pattern p46 = Pattern.compile(pattern46);
		Matcher m46= p46.matcher(userRequest);
		Pattern p47 = Pattern.compile(pattern47);
		Matcher m47= p47.matcher(userRequest);
		Pattern p48 = Pattern.compile(pattern48);
		Matcher m48 = p48.matcher(userRequest);
		Pattern p49 = Pattern.compile(pattern49);
		Matcher m49 = p49.matcher(userRequest);
		Pattern p50 = Pattern.compile(pattern50);
		Matcher m50 = p50.matcher(userRequest);
		Pattern p51 = Pattern.compile(pattern51);
		Matcher m51 = p51.matcher(userRequest);
		Pattern p52 = Pattern.compile(pattern52);
		Matcher m52 = p52.matcher(userRequest);
		Pattern p53 = Pattern.compile(pattern53);
		Matcher m53 = p53.matcher(userRequest);
		Pattern p54 = Pattern.compile(pattern54);
		Matcher m54 = p54.matcher(userRequest);
		Pattern p55 = Pattern.compile(pattern55);
		Matcher m55 = p55.matcher(userRequest);
		Pattern p56 = Pattern.compile(pattern56);
		Matcher m56 = p56.matcher(userRequest);
		Pattern p57 = Pattern.compile(pattern57);
		Matcher m57 = p57.matcher(userRequest);
		Pattern p58 = Pattern.compile(pattern58);
		Matcher m58 = p58.matcher(userRequest);
		Pattern p59 = Pattern.compile(pattern59);
		Matcher m59 = p59.matcher(userRequest);
		Pattern p60 = Pattern.compile(pattern60);
		Matcher m60 = p60.matcher(userRequest);
		Pattern p61 = Pattern.compile(pattern61);
		Matcher m61 = p61.matcher(userRequest);
		Pattern p62 = Pattern.compile(pattern62);
		Matcher m62 = p62.matcher(userRequest);
		Pattern p63 = Pattern.compile(pattern63);
		Matcher m63 = p63.matcher(userRequest);
		Pattern p64 = Pattern.compile(pattern64);
		Matcher m64 = p64.matcher(userRequest);
		Pattern p65 = Pattern.compile(pattern65);
		Matcher m65 = p65.matcher(userRequest);
		Pattern p66 = Pattern.compile(pattern66);
		Matcher m66 = p66.matcher(userRequest);
		Pattern p67 = Pattern.compile(pattern67);
		Matcher m67 = p67.matcher(userRequest);
		Pattern p68 = Pattern.compile(pattern68);
		Matcher m68 = p68.matcher(userRequest);
		Pattern p69 = Pattern.compile(pattern69);
		Matcher m69 = p69.matcher(userRequest);
		Pattern p70 = Pattern.compile(pattern70);
		Matcher m70 = p70.matcher(userRequest);
		Pattern p71 = Pattern.compile(pattern71);
		Matcher m71 = p71.matcher(userRequest);
		Pattern p72 = Pattern.compile(pattern72);
		Matcher m72 = p72.matcher(userRequest);
		Pattern p73 = Pattern.compile(pattern73);
		Matcher m73 = p73.matcher(userRequest);
		Pattern p74 = Pattern.compile(pattern74);
		Matcher m74 = p74.matcher(userRequest);
		Pattern p75 = Pattern.compile(pattern75);
		Matcher m75 = p75.matcher(userRequest);
		Pattern p76 = Pattern.compile(pattern76);
		Matcher m76 = p76.matcher(userRequest);
		/*Pattern p77 = Pattern.compile(pattern77);
		Matcher m77 = p77.matcher(userRequest);*/
		
		if (m4.find()) {
			ourUserIntent = UserIntent.now;
		} else if (m5.find()) {
			ourUserIntent = UserIntent.tree;
		} else if (m6.find()) {
			ourUserIntent = UserIntent.hello;
		} else if (m7.find()) {
			ourUserIntent = UserIntent.resume;
		} else if (m8.find()) {
			ourUserIntent = UserIntent.leave;
		} else if (m9.find()) {
			ourUserIntent = UserIntent.maybe;
		} else if (m10.find()) {
			ourUserIntent = UserIntent.today;
		} else if (m11.find()) {
			ourUserIntent = UserIntent.onne;
		/*} else if (m12.find()) {
			ourUserIntent = UserIntent.two;*/
		} else if (m13.find()) {
			ourUserIntent = UserIntent.vocabulary;
		} else if (m14.find()) {
			ourUserIntent = UserIntent.quiz;
		} else if (m15.find()) {
			ourUserIntent = UserIntent.easy;
		} else if (m16.find()) {
			ourUserIntent = UserIntent.medium;
		} else if (m17.find()) {
			ourUserIntent = UserIntent.hard;
		} else if (m18.find()) {
			ourUserIntent = UserIntent.playerone;
		} else if (m19.find()) {
			ourUserIntent = UserIntent.playertwo;
		} else if (m20.find()) {
			ourUserIntent = UserIntent.bye;
		} else if (m21.find()) {
			ourUserIntent = UserIntent.menu;
		} else if (m22.find()) {
			ourUserIntent = UserIntent.moin;
		} else if (m23.find()) {
			ourUserIntent = UserIntent.nextlevel;
		} else if (m24.find()) {
			ourUserIntent = UserIntent.banana;
		} else if (m25.find()) {
			ourUserIntent = UserIntent.again;
		} else if (m26.find()) {
			ourUserIntent = UserIntent.light;
		} else if (m27.find()) {
			ourUserIntent = UserIntent.bone;
		} else if (m28.find()) {
			ourUserIntent = UserIntent.twwo;
		} else if (m29.find()) {
			ourUserIntent = UserIntent.yes;
		} else if (m30.find()) {
			ourUserIntent = UserIntent.no;
		} else if (m31.find()) {
			ourUserIntent = UserIntent.quiz;
		} else if (m32.find()) {
			ourUserIntent = UserIntent.quit;
		} else if (m33.find()) {
			ourUserIntent = UserIntent.food;
		} else if (m34.find()) {
			ourUserIntent = UserIntent.head;
		} else if (m35.find()) {
			ourUserIntent = UserIntent.hair;
		} else if (m36.find()) {
			ourUserIntent = UserIntent.leg;
		} else if (m37.find()) {
			ourUserIntent = UserIntent.sun;
		} else if (m38.find()) {
			ourUserIntent = UserIntent.always;
		} else if (m39.find()) {
			ourUserIntent = UserIntent.water;
		} else if (m40.find()) {
			ourUserIntent = UserIntent.table;
		} else if (m41.find()) {
			ourUserIntent = UserIntent.city;
		} else if (m42.find()) {
			ourUserIntent = UserIntent.stairs;
		} else if (m43.find()) {
			ourUserIntent = UserIntent.haircolour;
		} else if (m44.find()) {
			ourUserIntent = UserIntent.wheel;
		} else if (m45.find()) {
			ourUserIntent = UserIntent.bellybutton;
		} else if (m46.find()) {
			ourUserIntent = UserIntent.broken;
		} else if (m47.find()) {
			ourUserIntent = UserIntent.contract;
		} else if (m48.find()) {
			ourUserIntent = UserIntent.community;
		} else if (m49.find()) {
			ourUserIntent = UserIntent.candle;
		} else if (m50.find()) {
			ourUserIntent = UserIntent.field;
		} else if (m51.find()) {
			ourUserIntent = UserIntent.gale;
		} else if (m52.find()) {
			ourUserIntent = UserIntent.giveup;
		} else if (m53.find()) {
			ourUserIntent = UserIntent.microwave;
		} else if (m54.find()) {
			ourUserIntent = UserIntent.pillow;
		} else if (m55.find()) {
			ourUserIntent = UserIntent.policy;
		} else if (m56.find()) {
			ourUserIntent = UserIntent.balance;
		} else if (m57.find()) {
			ourUserIntent = UserIntent.acquaintance;
		} else if (m58.find()) {
			ourUserIntent = UserIntent.bossy;
		} else if (m59.find()) {
			ourUserIntent = UserIntent.confident;
		} else if (m60.find()) {
			ourUserIntent = UserIntent.generous;
		} else if (m61.find()) {
			ourUserIntent = UserIntent.middleclass;
		} else if (m62.find()) {
			ourUserIntent = UserIntent.motherinlaw;
		} else if (m63.find()) {
			ourUserIntent = UserIntent.moody;
		} else if (m64.find()) {
			ourUserIntent = UserIntent.reliable;
		} else if (m65.find()) {
			ourUserIntent = UserIntent.accountancy;
		} else if (m66.find()) {
			ourUserIntent = UserIntent.apply;
		} else if (m67.find()) {
			ourUserIntent = UserIntent.fluently;
		} else if (m68.find()) {
			ourUserIntent = UserIntent.insist;
		} else if (m69.find()) {
			ourUserIntent = UserIntent.representative;
		} else if (m70.find()) {
			ourUserIntent = UserIntent.smoothly;
		} else if (m71.find()) {
			ourUserIntent = UserIntent.bewillingto;
		} else if (m72.find()) {
			ourUserIntent = UserIntent.difficulty;
		} else if (m73.find()) {
			ourUserIntent = UserIntent.vocab;
		} else if (m74.find()) {
			ourUserIntent = UserIntent.yess;
		} else if (m75.find()) {
			ourUserIntent = UserIntent.hey;
		} else if (m76.find()) {
			ourUserIntent = UserIntent.hand;
		/*} else if (m77.find()) {
			ourUserIntent = UserIntent.button;*/
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
		case 0: 
			speech.setSsml("<speak><amazon:effect name=\"whispered\">" + text + "</amazon:effect></speak>");
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
			speech.setSsml("<speak><audio src=\"soundbank://soundlibrary/ui/gameshow/amzn_ui_sfx_gameshow_intro_01\"/><amazon:emotion name=\"excited\" intensity=\"high\">" + text + "</amazon:emotion></speak>");
			break;
			
		// in Kombination mit goodbyeMsg (GAMESHOW OUTRO SOUND):
		case 6: 
			speech.setSsml("<speak><amazon:emotion name=\"excited\" intensity=\"medium\">" + text + "</amazon:emotion><audio src=\"soundbank://soundlibrary/ui/gameshow/amzn_ui_sfx_gameshow_outro_01\" /></speak>");
			break;
			
		default: 
			speech.setSsml("<speak><amazon:effect name=\"whispered\">" + text + "</amazon:effect></speak>");
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
		repromptSpeech.setSsml("<speak><emphasis level=\"strong\">Hey!</emphasis> Are you still there?</speak>");

		Reprompt rep = new Reprompt();
		rep.setOutputSpeech(repromptSpeech);

		return SpeechletResponse.newAskResponse(speech, rep);
	}


}
