package Problem_Domain;

import java.text.SimpleDateFormat;
import java.util.Calendar;

public class Message implements java.io.Serializable
{
	private static final long serialVersionUID = 4412316044526006384L;
	private final static byte DEFAULT_EXPIRE_DATE = 3;
	private String messageDetail;
	private Calendar createdDate;
	private Calendar expiredDate;
	private String createdBy;
	
	/**
	 * 메세지 객체 생성자.
	 * @param detail 내용
	 * @param until 게시 만료일자
	 * @param created 메세지 생성한 사용자
	 */
	protected Message(String detail, String created, Calendar until)
	{
		messageDetail = detail;
		createdDate = Calendar.getInstance();
		expiredDate = until;
		createdBy = created;
	}
	
	protected Message(String detail, String created)
	{
		this(detail, created, getAfterDay());
	}
	
	public boolean isExpired() { return expiredDate.before(Calendar.getInstance()); }
	protected String getcreatedBy() { return createdBy; }
	protected Calendar getCreatedDate() { return createdDate; }
	protected Calendar getEndDate() { return expiredDate; }
	protected String getMessageDetail() { return messageDetail; }

	
	/**
	 * 현재 날짜에서 cnt일 후의 날짜를 계산해서 반환함
	 * @param cnt 몇일 후 지정
	 * @return 계산한 날짜
	 */
	private static Calendar getAfterDay(int cnt)
	{
		Calendar date = Calendar.getInstance();
		date.add(Calendar.DATE, cnt);
		return date;
	}
	
	/**
	 * 기본 만료일 후의 날짜를 계산해서 반환함
	 * @return 기본 만료일 후의 날짜
	 */
	private static Calendar getAfterDay() { return getAfterDay(DEFAULT_EXPIRE_DATE); }
	
	/**
	 * 메세지 문자열로 변환<p>
	 * 변환 방식은 "[생성일자/생성인] '메세지 내용'"
	 */
	public String toString()
	{
		SimpleDateFormat sdf = new SimpleDateFormat("yyyy/MM/dd");
		return '[' + sdf.format(getCreatedDate().getTime()) + ',' + getcreatedBy() + "] " + getMessageDetail();
	}
}
