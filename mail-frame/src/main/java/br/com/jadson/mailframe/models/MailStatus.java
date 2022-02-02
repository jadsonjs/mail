package br.com.jadson.mailframe.models;

/**
 * Mail Status
 */
public enum MailStatus {
    /** The mail is in the queue to be send */
    PROCESSING,
    /** The mail was send with success */
    SENT,
    /** There was a error and the mail could not be sent*/
    ERROR;
}
