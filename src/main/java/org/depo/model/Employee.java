package org.depo.model;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlRootElement;

@XmlRootElement(name = "employee")
@XmlAccessorType(XmlAccessType.FIELD)
public class Employee {

	@XmlElement(name = "id")
    private int id; //Табельный номер сотрудника (ЦЕЛОЕ ЧИСЛО)

	@XmlElement(name = "fio")
	private String fio;//ФИО (СТРОКА)

	@XmlElement(name = "position")
	private String position;//Должность (СТРОКА)

	@XmlElement(name = "email")
	private String email;//Адрес электронной почты (СТРОКА)

	@XmlElement(name = "tel")
	private String tel;//Номер телефона (СТРОКА)

	@XmlElement(name = "tel2")
	private String tel2;//Доп. номер телефона (СТРОКА)

	@XmlElement(name = "room")
	private String room;//Номер кабинета (СТРОКА)

	public int getId() {
		return id;
	}

	public void setId(int id) {
		this.id = id;
	}

	public String getFio() {
		return fio;
	}

	public void setFio(String fio) {
		this.fio = fio;
	}

	public String getPosition() {
		return position;
	}

	public void setPosition(String position) {
		this.position = position;
	}

	public String getEmail() {
		return email;
	}

	public void setEmail(String email) {
		this.email = email;
	}

	public String getTel() {
		return tel;
	}

	public void setTel(String tel) {
		this.tel = tel;
	}

	public String getTel2() {
		return tel2;
	}

	public void setTel2(String tel2) {
		this.tel2 = tel2;
	}

	public String getRoom() {
		return room;
	}

	public void setRoom(String room) {
		this.room = room;
	}

	@Override
	public String toString() {
		return "EMPLOYEE-" + id + fio + ',' + position + ',' + email + 'm' + tel + "," + tel2 + "," + room;
	}
}
