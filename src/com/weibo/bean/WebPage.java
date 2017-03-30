package com.weibo.bean;

import org.jsoup.nodes.Document;

public class WebPage {
	/**
	 * 网页和其基本信息。
	 */
	public WebPage() {

	}

	/** 页面链接 */
	private String href;

	/** 页面对应的HTML源码 */
	private String html;

	/** 页面大小 */
	private int contentLength;

	/** 页面标题 */
	private String title;

	/** 页面正文 */
	private String content;

	/** 页面类型 */
	private String contentType;

	/** 页面编码 */
	private String contentEncoding;

	/** 文档对象 */
	private Document contentDoc;

	public WebPage(String content, Document contentDoc) {
		this.content = content;
		this.contentDoc = contentDoc;
	}

	public String getHref() {
		return href;
	}

	public void setHref(String href) {
		this.href = href;
	}

	public String getHtml() {
		return html;
	}

	public void setHtml(String html) {
		this.html = html;
	}

	public int getContentLength() {
		return contentLength;
	}

	public void setContentLength(int contentLength) {
		this.contentLength = contentLength;
	}

	public String getTitle() {
		return title;
	}

	public void setTitle(String title) {
		this.title = title;
	}

	public String getContent() {
		return content;
	}

	public void setContent(String content) {
		this.content = content;
	}

	public String getContentType() {
		return contentType;
	}

	public void setContentType(String contentType) {
		this.contentType = contentType;
	}

	public String getContentEncoding() {
		return contentEncoding;
	}

	public void setContentEncoding(String contentEncoding) {
		this.contentEncoding = contentEncoding;
	}

	public Document getContentDoc() {
		return contentDoc;
	}

	public void setContentDoc(Document contentDoc) {
		this.contentDoc = contentDoc;
	}

}
