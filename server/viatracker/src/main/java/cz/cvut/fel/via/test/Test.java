/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package cz.cvut.fel.via.test;

import cz.cvut.fel.via.IssueResource;

/**
 *
 * @author Vasek
 */
public class Test {
    
    public static void main(String[] args) {
        
        IssueResource is = new IssueResource();
        
        is.addNewIssue("[\n" +
" {\n" +
"\"title\": \"Second Issue\",\n" +
"\"description\": \"Issue description. bleee..\",\n" +
"\"priority\": 3,\n" +
"\"createdByUser\": 2\n" +
"}\n" +
"]");
        
    }
    
}
