package com.weibo.bean;

public class Weibo 
{
	
	private String poster = null;

	//picture or not
	private boolean hasPic = false;
	
	/** 微博内容 */
	private String content;
	/** 微博Mid */
	private String mid; //id
	
	/** 是否是转发微博： 0：不是，1：是 */
	private boolean isRepost = false;
	
	/** 微博作者 */
	private String authorUrl;
	/** 微博发布者名称 */
	private String[] authorName;
	/** 微博认证 */
	private String identity;
	/** 微博转发数 */
	private String forward;
	/** 微博收藏数 */
	private String favorite;
	/** 微博评论数 */
	private String comment;
	
	/** 微博发布时间 */
	private String date;
	//posttime
	/** 微博发布时间 */
	private String dateString;
	
	/** 微博发布来源 */
	private String comeFrom;
	/**  */
	private String mapData;
	/** 赞的数量 */
	private String heart;
	/** 作者ID */
	private String userId;
	/** 微博访问地址 */
	private String href;
	public String getContent() {
		return content;
	}
	public void setContent(String content) {
		this.content = content;
	}
	public String getMid() {
		return mid;
	}
	public void setMid(String mid) {
		this.mid = mid;
	}
	
	public boolean isRepost() {
		return isRepost;
	}

	public void setRepost(boolean isRepost) {
		this.isRepost = isRepost;
	}
	
	public String getAuthorUrl() {
		return authorUrl;
	}
	public void setAuthorUrl(String authorUrl) {
		this.authorUrl = authorUrl;
	}
	public String[] getAuthorName(String[] authorname) {
		return authorName;
	}
	public void setAuthorName(String[] authorname) {
		this.authorName = authorname;
	}
	public String getIdentity() {
		return identity;
	}
	public void setIdentity(String identity) {
		this.identity = identity;
	}
	public String getForward() {
		return forward;
	}
	public void setForward(String forward) {
		this.forward = forward;
	}
	public String getFavorite() {
		return favorite;
	}
	public void setFavorite(String favorite) {
		this.favorite = favorite;
	}
	public String getComment() {
		return comment;
	}
	public void setComment(String comment) {
		this.comment = comment;
	}
	public String getDate() {
		return date;
	}
	public void setDate(String date) {
		this.date = date;
	}
	public String getDateString() {
		return dateString;
	}
	public void setDateString(String dateString) {
		this.dateString = dateString;
	}
	public String getComeFrom() {
		return comeFrom;
	}
	public void setComeFrom(String comeFrom) {
		this.comeFrom = comeFrom;
	}
	public String getMapData() {
		return mapData;
	}
	public void setMapData(String mapData) {
		this.mapData = mapData;
	}
	public String getHeart() {
		return heart;
	}
	public void setHeart(String heart) {
		this.heart = heart;
	}
	public String getUserId() {
		return userId;
	}
	public void setUserId(String userId) {
		this.userId = userId;
	}
	public String getHref() {
		return href;
	}
	public void setHref(String href) {
		this.href = href;
	}
	
	
	public String getPoster() {
		return poster;
	}
	
	public void setPoster(String poster) {
		this.poster = poster;
	}
	public boolean isHasPic() {
		return hasPic;
	}

	public void setHasPic(boolean hasPic) {
		this.hasPic = hasPic;
	}
	
	@Override
	public String toString(){
		StringBuilder sb = new StringBuilder();
		
		sb.append("id:\t\t").append(mid).append("\n")
		  .append("poster:\t\t").append(poster).append("\n")
		  .append("content:\t").append(content).append("\n")
		  .append("hasPic:\t\t").append(hasPic).append("\n")
		  .append("isRepost:\t").append(isRepost).append("\n");
		
		return sb.toString();
	}
	
	
}
