require 'rdf'
require 'rdf/rdfxml'
require 'rdf/virtuoso'


# Database connection
# =============================
DB_uri = "http://vrtestbed.iis.sinica.edu.tw:8890/sparql"
DB_update_uri = "http://vrtestbed.iis.sinica.edu.tw:8890/sparql-auth"

repo = RDF::Virtuoso::Repository.new(DB_uri, :username => 'dba', :password => 'iis404')

# Load RDF files
# =============================
File_path = "/Users/uplaabura/Dropbox/OpenISDM/VR/Data_Sources/Debries_Alert/debries_sample_rdf.xml"

rdf_xml_set = RDF::RDFXML::Reader.open(File_path)

# Prepare & execute query
# =============================
Virtuoso_graph = "http://mygraph.com.tw"

rdf_xml_set.each_triple do |sub, pred, obj|
	puts [sub, pred, obj].inspect
	query_string = RDF::Virtuoso::Query.insert([sub, pred, obj]).graph(Virtuoso_graph)
	result = repo.insert(query_string)
end

#RDF::RDFXML::Reader.open(file_path) do |reader|
#	reader.each_statement do |statement|
#		#puts statement.statement?
#		query = QUERY.insert([statement.subject, statement.predicate, statement.object]).graph(graph).where([:s, :p, :o])
#		#query = "INSERT INTO GRAPH #{graph} { #{statement.to_s} }"
#		result = repo.insert(query)
#	end
#end
