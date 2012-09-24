require 'rubygems'
require 'nokogiri'
require 'open-uri'

	url = "http://2rd.taiwan.net.tw/Forms/FQ0141.aspx?strFunctionIDValue=strFunctionID=FQ0141"
	selector = ".attraction_title"

	html_doc = Nokogiri::HTML(open(url))
	obj = html_doc.css("#{selector}").text

	puts obj
