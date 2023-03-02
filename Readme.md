In this project, I have created a source folder in which,I put
3 different jsons files to create the MySQL connector.

1. with where clause:
When you do incrementing column in that case there is no need to mention where condition
because by default it will use "where ID>1" and this is incremental column that we have defined in the
"incrementing.column.name": "ID" property.

{
"name": "mysql-jdbc",
"config": {
"connector.class" : "io.confluent.connect.jdbc.JdbcSourceConnector",
"connection.url"  : "jdbc:mysql://mysqldb:3306/silicon",
"connection.user" : "root",
"connection.password" : "root",
"mode"            : "incrementing",
"incrementing.column.name": "id",
"query": "select * from (select * from credit_lines) test",
"topic.prefix"    : "JDBC.test_db_test",
"validate.non.null"       : "false",
"poll.interval.ms"        : "1000"
}
}

you can take the reference of this branch too:
https://github.com/Maninder416/apache-kafka/blob/feature/silicon-valley-project/source/docker/flex-creditline-table-13.json