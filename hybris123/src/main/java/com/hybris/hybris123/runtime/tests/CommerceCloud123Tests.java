package com.hybris.hybris123.runtime.tests;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotEquals;
import static org.junit.Assert.assertTrue;
import static org.junit.Assert.fail;

import java.net.URI;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;
import java.net.http.HttpResponse.BodyHandlers;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.Arrays;


import org.junit.Test;


public class CommerceCloud123Tests {

    @Test
    public void testCommerceCloudCheck() throws Exception {
        /**
         * Confirm that the environmnet variables are set:
         * export DOWNLOAD_DIR=<the directory to which your browser downloads files> 
         * export GITHUB_USERNAME=<your Github username> 
         * export GITHUB_TOKEN=<a Github Token with repo and admin:public_key rights. You can create this @ https://github.com/settings/tokens> 
         * export HYBRIS_HOME_DIR=<the absolute path to a location on your drive where you will do this trail.> 
         * export SAP_COMMERCE=<The version of SAP Commerce you will use from SAP SoftwareDownloads web site, without the ".zip" suffix, for example CXCOMM201100P_11-70005693>
         * cd $HYBRIS_HOME_DIR/hybris123; mvn test -Dtest=com.hybris.hybris123.runtime.tests.CommerceClou123Tests#testCommerceCloudCheck test
         */
        java.util.Map<String, String> environmentVariables = System.getenv();
        assertTrue("Expected Environment variables are missing.", environmentVariables.keySet().containsAll(
            Arrays.asList("DOWNLOAD_DIR", "GITHUB_USERNAME", "GITHUB_TOKEN", "HYBRIS_HOME_DIR", "SAP_COMMERCE")));

        // Confirm your github account contains the repo concerttours-ccloud
        HttpClient client = HttpClient.newBuilder().build();
        HttpRequest request = HttpRequest.newBuilder()
                .uri(URI.create(String.format("https://api.github.com/repos/%s/concerttours-ccloud",  environmentVariables.get("GITHUB_USERNAME"))))
                .header("u", String.format("%s", environmentVariables.get("GITHUB_USERNAME")))
                .header("Authorization", String.format("token %s", environmentVariables.get("GITHUB_TOKEN"))).build();
        HttpResponse<String> response = client.send(request, BodyHandlers.ofString());
        assertEquals("The Github Repo concerttours-ccloud was not found in your Github account.", 200,  response.statusCode());

        // Cloning the Cloud Commerce Sample Repo should create this directory..
        Path path = Paths.get(String.format("%s/cloud-commerce-sample-setup", environmentVariables.get("HYBRIS_HOME_DIR")));
        assertTrue("It appears you have not yet cloned cloud-commerce-sample-setup, as directory " + path   + " was not found", Files.exists(path));

        // Unzipping SAP Commerce should create many directories including this one
        path = Paths.get(String.format("%s/%s/hybris/bin", environmentVariables.get("HYBRIS_HOME_DIR"), environmentVariables.get("SAP_COMMERCE")));
        assertTrue("It appears you have not yet (downloaded?) and unzipped SAP Commerce, as " + path + " was not found",  Files.exists(path));

        // Copying over the modules and platform folders should create these directories
        path = Paths.get(String.format("%s/cloud-commerce-sample-setup/core-customize/hybris/bin/modules",  environmentVariables.get("HYBRIS_HOME_DIR")));
        assertTrue("It appears you have not yet copied over the modules folder, as " + path + " was not found", Files.exists(path));
        path = Paths.get(String.format("%s/cloud-commerce-sample-setup/core-customize/hybris/bin/platform",  environmentVariables.get("HYBRIS_HOME_DIR")));
        assertTrue("It appears you have not yet copied over the platfrom folder, as " + path + " was not found",   Files.exists(path));

        // Calling "ant addoninstall" should create some new directories including this one..
        path = Paths.get(String.format("%s/cloud-commerce-sample-setup/core-customize/hybris/data",  environmentVariables.get("HYBRIS_HOME_DIR")));
        assertTrue("It appears you have not yet called 'ant addoninstall', as " + path + " was not found",  Files.exists(path));

        // Calling "ant initialize" should create some new directories including this one..
        path = Paths.get(String.format("%s/cloud-commerce-sample-setup/core-customize/hybris/bin/platform/tomcat",   environmentVariables.get("HYBRIS_HOME_DIR")));
        assertTrue("It appears you have not yet called 'ant initialize', as " + path + " was not found",   Files.exists(path));

        client = HttpClient.newBuilder().build();
        request = HttpRequest.newBuilder().uri(URI.create("http://localhost:9001")).build();
        try {
            response = client.send(request, BodyHandlers.ofString());
        } catch (java.net.ConnectException e) {
            fail("Could not access Commerce @ http://localhost:9001. Have you started the server?");
        } finally {
            assertNotEquals("Could not access Commerce @ http://localhost:9001. Have you started the server?", 404, response.statusCode());
        }

        client = HttpClient.newBuilder().build();
        request = HttpRequest.newBuilder().uri(URI.create("https://localhost:4200. Have you called yarn start?")).build();
        try {
            response = client.send(request, BodyHandlers.ofString());
        } catch (java.io.IOException e) {
            // Expect an https-related error to be thrown, but what matters is the assertion in the finally block
        } finally {
            assertNotEquals(  "Could not access access Spartacus Storefront @ https://localhost:4200. Have you called yarn start?", 404, response.statusCode());
        }
    }

}