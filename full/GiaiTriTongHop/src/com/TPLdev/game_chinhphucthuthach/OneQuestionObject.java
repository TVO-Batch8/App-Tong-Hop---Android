package com.TPLdev.game_chinhphucthuthach;

public class OneQuestionObject {
	public int id, level;
	public String question, answer_a, answer_b, answer_c, answer_d;

	// ham tao ko doi so dung trong class QuestionManagerDatabase
	// tao doi tuong ra sau do set cac thuoc tinh
	// coi ham getNQuestionRandom
	public OneQuestionObject() {

	}

	// ham tao day du
	public OneQuestionObject(int id, String ch, int lv, String a, String b,
			String c, String d) {
		this.id = id;
		this.question = ch;
		this.level = lv;
		this.answer_a = a;
		this.answer_b = b;
		this.answer_c = c;
		this.answer_d = d;
	}
}
