package jp.ac.fukushima_u.gp.template.mail;

public class GmailSend{
	public static void main(String[] args) 
    {
        //MailSender oMail = new MailSender( "smtp.sample.com", 25 );
        //Gmail���M
        MailSender oMail = new MailSender( "smtp.gmail.com", 465, "summerswallow0000@gmail.com","0tjadgjmptw" );

        oMail.addToAddress( "summerswallow0000@yahoo.co.jp","���M��" );
//        oMail.addToAddress( "xx2@local.jp" );
//        oMail.addCCAddress( "xx3@local.jp" );
//        oMail.addBCCAddress( "xx4@local.jp","BCC" );
        oMail.setFromAddress( "xx@com","���M��" );
        oMail.setSubject("�����e�X�g" );
        oMail.setBody( "�{���e�X�g" );
        //oMail.addAttachment( "filepath" );
        String path ="C:\\hoge\\javax.mail.jar";
        oMail.addAttachment( path );


        try
        {
            oMail.sendMail();
        }
        catch( Exception e )
        {
            System.out.println( e.toString() );
            e.printStackTrace();
        }

        System.exit(0);
    }
}
