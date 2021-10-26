See https://wiki.wdf.sap.corp/wiki/display/prodandtech/SandBox+for+CommerceCloud123

chmod 700 junit-platform-console-standalone-1.8.0-M1.jar

javac -d target -cp junit-platform-console-standalone-1.8.0-M1.jar src/main/java/com/hybris/hybris123/runtime/tests/CommerceCloud123Tests.java

java -jar junit-platform-console-standalone-1.8.0-M1.jar --class-path target --select-class com.hybris.hybris123.runtime.tests.CommerceCloud123Tests
# commercecloud123testsandbox
