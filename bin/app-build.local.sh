sudo rm $PWD/server/deployment/model.war

cd $PWD/app

maven clean install

cd ..

sudo mv $PWD/app/target/model-0.1.war $PWD/server/deployment/model.war
