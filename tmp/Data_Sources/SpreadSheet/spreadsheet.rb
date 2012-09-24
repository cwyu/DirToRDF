require "spreadsheet"

base_uri = "http://openisdm/#"
Spreadsheet.client_encoding = 'UTF-8'
mac_file_path = "/Users/uplaabura/Dropbox/OpenISDM/VR/Data_Sources/SpreadSheet/Tour_Companies.xls"
linux_file_path = "/home/openisdm/Dropbox/OpenISDM/VR/Data_Sources/SpreadSheet/Tour_Companies.xls"

book = Spreadsheet.open linux_file_path	
sheet1 = book.worksheet 0
schema = sheet1.row(1)


sheet1.each 2 do |row|	# "2" means the started row is the third row

	sub = base_uri+row[1].to_s

	for i in 0..schema.length

		pred = base_uri+schema[i].to_s
		obj = base_uri+row[i].to_s

		puts sub+", "+pred+", "+obj

	end


end




