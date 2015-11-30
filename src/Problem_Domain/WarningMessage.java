package Problem_Domain;

import java.util.Calendar;

public class WarningMessage extends Message
{
	private static final long serialVersionUID = 3407002089938496042L;

	/**
	 * 특정 종료일자를 지정할 경우 사용할 생성자
	 * @param detail 메세지 내용
	 * @param created 메세지 제작자
	 * @param messageUntil 게시만료일자
	 */
	public WarningMessage(String detail, String created, Calendar until)
	{
		super(detail, created, until);
	}
	
	/**
	 * 특정 종료일자를 지정하지 않을 경우 사용할 생성자
	 * @param detail 메세지 내용
	 * @param created 메세지 제작자
	 */
	public WarningMessage(String detail, String created)
	{
		super(detail, created);
	}
	
	/**
	 * WarningMessage 메세지 문자열로 변환
	 * 변환 방식은 "[생성 일자/생성인] Warning : '메세지 내용'"
	 */
	public String toString()
	{
		String[] splitted = super.toString().split(" ");
		return splitted[0] + "Warning : " + splitted[1];
		
	}
	
}
