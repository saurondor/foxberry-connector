/**
 * Copyright (c) 2014, Gerardo Tasistro, Tiempo Meta
 * All rights reserved. Todos los derechos reservados.
 */
package com.tiempometa.muestradatos;

/**
 * @author Gerardo Tasistro gtasistro@tiempometa.com
 *
 */
public class SystemProperties {
	String file_separator = System.getProperty("file.separator");//	Character that separates components of a file path. This is "/" on UNIX and "\" on Windows.
	String java_class_path = System.getProperty("java.class.path");//	Path used to find directories and JAR archives containing class files. Elements of the class path are separated by a platform-specific character specified in the path.separator property.
	String java_home = System.getProperty("java.home");//	Installation directory for Java Runtime Environment (JRE)
	String java_vendor = System.getProperty("java.vendor");//	JRE vendor name
	String java_vendor_url = System.getProperty("java.vendor.url");//	JRE vendor URL
	String java_version = System.getProperty("java.version");//	JRE version number
	String line_separator = System.getProperty("line.separator");//	Sequence used by operating system to separate lines in text files
	String os_arch = System.getProperty("os.arch");//	Operating system architecture
	String os_name = System.getProperty("os.name");//	Operating system name
	String os_version = System.getProperty("os.version");//	Operating system version
	String path_separator = System.getProperty("path.separator");//	Path separator character used in java.class.path
	String user_dir = System.getProperty("user.dir");//	User working directory
	String user_home = System.getProperty("user.home");//	User home directory
	String user_name = System.getProperty("user.name");//	User account name
	String arch_data_model = System.getProperty("sun.arch.data.model");
	public String getFile_separator() {
		return file_separator;
	}
	public void setFile_separator(String file_separator) {
		this.file_separator = file_separator;
	}
	public String getJava_class_path() {
		return java_class_path;
	}
	public void setJava_class_path(String java_class_path) {
		this.java_class_path = java_class_path;
	}
	public String getJava_home() {
		return java_home;
	}
	public void setJava_home(String java_home) {
		this.java_home = java_home;
	}
	public String getJava_vendor() {
		return java_vendor;
	}
	public void setJava_vendor(String java_vendor) {
		this.java_vendor = java_vendor;
	}
	public String getJava_vendor_url() {
		return java_vendor_url;
	}
	public void setJava_vendor_url(String java_vendor_url) {
		this.java_vendor_url = java_vendor_url;
	}
	public String getJava_version() {
		return java_version;
	}
	public void setJava_version(String java_version) {
		this.java_version = java_version;
	}
	public String getLine_separator() {
		return line_separator;
	}
	public void setLine_separator(String line_separator) {
		this.line_separator = line_separator;
	}
	public String getOs_arch() {
		return os_arch;
	}
	public void setOs_arch(String os_arch) {
		this.os_arch = os_arch;
	}
	public String getOs_name() {
		return os_name;
	}
	public void setOs_name(String os_name) {
		this.os_name = os_name;
	}
	public String getOs_version() {
		return os_version;
	}
	public void setOs_version(String os_version) {
		this.os_version = os_version;
	}
	public String getPath_separator() {
		return path_separator;
	}
	public void setPath_separator(String path_separator) {
		this.path_separator = path_separator;
	}
	public String getUser_dir() {
		return user_dir;
	}
	public void setUser_dir(String user_dir) {
		this.user_dir = user_dir;
	}
	public String getUser_home() {
		return user_home;
	}
	public void setUser_home(String user_home) {
		this.user_home = user_home;
	}
	public String getUser_name() {
		return user_name;
	}
	public void setUser_name(String user_name) {
		this.user_name = user_name;
	}
	public String getArch_data_model() {
		return arch_data_model;
	}
	public void setArch_data_model(String arch_data_model) {
		this.arch_data_model = arch_data_model;
	}
	@Override
	public String toString() {
		return "SystemProperties [file_separator=" + file_separator
				+ ", java_class_path=" + java_class_path + ", java_home="
				+ java_home + ", java_vendor=" + java_vendor
				+ ", java_vendor_url=" + java_vendor_url + ", java_version="
				+ java_version + ", line_separator=" + line_separator
				+ ", os_arch=" + os_arch + ", os_name=" + os_name
				+ ", os_version=" + os_version + ", path_separator="
				+ path_separator + ", user_dir=" + user_dir + ", user_home="
				+ user_home + ", user_name=" + user_name + ", arch_data_model="
				+ arch_data_model + "]";
	}
}
