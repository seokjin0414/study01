package com.company.nill.myTool.utill;

import org.apache.velocity.Template;
import org.apache.velocity.VelocityContext;
import org.apache.velocity.app.VelocityEngine;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import java.io.StringWriter;
import java.util.Map;
import java.util.Properties;

@Component
public class VelocityUtil {

	@Value("${common.template.mail}")
	private String templatePath;

	/**
	 * Email Template
	 * @param contextMap
	 * @param fileName
	 * @return
	 * @throws Exception
	 */
	public String getMailContent(Map<String, Object> contextMap, String fileName) throws Exception{
		String path = templatePath +""+ fileName;
		return getContentFromTemplate(contextMap,path);
	}

	@SuppressWarnings("rawtypes")
	private String getContentFromTemplate(Map<String, Object> contextMap, String fileName) throws Exception{
		//String path = "/WEB-INF/classes/";
		Properties p = new Properties();
		p.setProperty("resource.loader", "class");
		p.setProperty("class.resource.loader.class", "org.apache.velocity.runtime.resource.loader.ClasspathResourceLoader");

		VelocityEngine ve = new VelocityEngine();
		ve.init(p);
		VelocityContext context = new VelocityContext();

		contextMap.put("messageUtil", MessageUtil.class);

		String vmFileName = fileName;
		for (Map.Entry entry : contextMap.entrySet()) {
		  context.put((String)entry.getKey(), entry.getValue());
		}

		Template t = ve.getTemplate(vmFileName,"UTF-8");
		StringWriter writer = new StringWriter();
		t.merge(context, writer);
		return writer.toString();
	}
}
