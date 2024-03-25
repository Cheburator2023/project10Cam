rm $PWD/server/deployment/model.war

cd $PWD/app

mvn clean install

cd ..

mv $PWD/app/target/model-0.1.war $PWD/server/deployment/model.war
