package jp.ac.fukushima_u.gp.template.mail;
import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.util.ArrayList;
import java.util.Date;
import java.util.Enumeration;
import java.util.Hashtable;
import java.util.List;
import java.util.Properties;

import javax.activation.DataHandler;
import javax.activation.FileDataSource;
import javax.mail.Address;
import javax.mail.Message;
import javax.mail.MessagingException;
import javax.mail.Multipart;
import javax.mail.PasswordAuthentication;
import javax.mail.Session;
import javax.mail.Transport;
import javax.mail.internet.AddressException;
import javax.mail.internet.InternetAddress;
import javax.mail.internet.MimeBodyPart;
import javax.mail.internet.MimeMessage;
import javax.mail.internet.MimeMultipart;
import javax.mail.internet.MimeUtility;

public class MailSender 
{

    /**
     * ���[�����M�G���[
     *
     */
    public class SenderException extends Exception 
    {
        public SenderException( Exception e ) 
        {
            super( e );
        }
        private static final long serialVersionUID = 1L;        
    };

    /** �G���R�[�h ��`*/
    private final String Encode_ISO2022 = "ISO-2022-JP";

    /** �G���R�[�h ��`*/
    private final String Encode_UTF8 = "UTF-8";

    /** SSL */
    private final String SSL_FACTORY = "javax.net.ssl.SSLSocketFactory";

    /** ���M��/�ʖ����X�g */
    private Hashtable<String, String> listTo = new Hashtable<String, String>();
    /** CC/�ʖ����X�g */
    private Hashtable<String, String> listCC = new Hashtable<String, String>();
    /** BCC/�ʖ����X�g */
    private Hashtable<String, String> listBCC = new Hashtable<String, String>();
    /** ���M��/�ʖ� */
    private Hashtable<String, String> pearFrom = new Hashtable<String, String>(1);

    /** �Y�t�t�@�C�� */
    private List<String> listFilePath = new ArrayList<String>();

    /** �G���R�[�h */
    private String _AttachmentFileNameEncode = Encode_ISO2022;

    /** �G���R�[�h */
    private String _AddressAliasEncode = Encode_ISO2022; 

    /** �G���R�[�h */
    private String _SubjectEncode = Encode_ISO2022;

    /** �G���R�[�h */
    private String _TextEncode = Encode_ISO2022;

    /** ���� */
    private String _Subject = "Subject";

    /** �{�� */
    private String _Body = "Body";

    /** SMTP�T�[�o */
    private String _SMTP = "localhost";

    /** �|�[�g */
    private int _Port = 25;

    /** �f�o�b�O */
    private boolean _Dubug = false;

    /** �F�� */
    private boolean _Auth = false;

    /** �F��ID */
    private String _AuthUserName = "";

    /** �F�؃p�X���[�h */
    private String _AuthPassWord = "";


    /**
     * ���M�Ҋi�[
     * @param sFrom ���M�҃A�h���X
     */
    public void setFromAddress( String sFrom )
    {
        pearFrom.clear();
        pearFrom.put( sFrom, "" );
    }

    /**
     * ���M�Ҋi�[
     * @param sFrom ���M�҃A�h���X
     * @param sAlias ���M�ҕʖ�
     */
    public void setFromAddress( String sFrom, String sAlias )
    {
        pearFrom.clear();
        pearFrom.put( sFrom, sAlias );
    }

    /**
     * ���M�҂��i�[����
     * @param sTo ���M�҃A�h���X
     * @throws AddressException
     */
    public void addToAddress( String sTo )
    {
        listTo.put( sTo, "" );
    }

    /**
     * ���M�҂��i�[����
     * @param sTo ���M�҃A�h���X
     * @param sAlias ���M�ҕʖ�
     * @throws AddressException
     */
    public void addToAddress( String sTo, String sAlias )
    {
        listTo.put( sTo, sAlias );
    }

    /**
     * CC���i�[����
     * @param sCC CC�A�h���X
     * @throws AddressException
     */
    public void addCCAddress( String sCC )
    {
        listCC.put( sCC, "" );
    }

    /**
     * CC���i�[����
     * @param sCC CC�A�h���X
     * @param sAlias CC�ʖ�
     * @throws AddressException
     */
    public void addCCAddress( String sCC, String sAlias )
    {
        listCC.put( sCC, sAlias );
    }

    /**
     * BCC���i�[����
     * @param sBCC BCC�A�h���X
     * @throws AddressException
     */
    public void addBCCAddress( String sBCC )
    {
        listBCC.put( sBCC, "" );
    }

    /**
     * BCC���i�[����
     * @param sBCC BCC�A�h���X
     * @param sAlias BCC�ʖ�
     * @throws AddressException
     */
    public void addBCCAddress( String sBCC, String sAlias )
    {
        listBCC.put( sBCC, sAlias );
    }



    /**
     * �������i�[����
     * @param sSubject ����
     */
    public void setSubject ( String sSubject )
    {
        this._Subject = sSubject;

    }

    /**
     * �{�����i�[����
     * @param sBody �{��
     */
    public void setBody ( String sBody )
    {
        this._Body = sBody;
    }

    /**
     * �Y�t�t�@�C���p�X���X�g
     * @param sFilePath
     */
    public void addAttachment( String sFilePath )
    {
        listFilePath.add( sFilePath );
    }

    /**
     * �R���X�g���N�^
     * �f�t�H���g�l
     * SMTP�Flocalhost
     * Port�F25
     */
    public MailSender( )
    {}

    /**
     * �R���X�g���N�^
     * �f�t�H���g�l
     * Port�F25
     */
    public MailSender( String sSMTP )
    {
        this._SMTP = sSMTP;
    }

    /**
     * �R���X�g���N�^
     * @param sSMTP SMTP�T�[�o
     * @param iPort �|�[�g
     */
    public MailSender( String sSMTP, int iPort ) 
    {
        this._SMTP = sSMTP;
        this._Port = iPort;

    }

    /**
     * �R���X�g���N�^
     * @param sSMTP SMTP
     * @param iPort �|�[�g
     * @param sUserName �F��ID
     * @param sPassWord �F�؃p�X���[�h
     */
    public MailSender( String sSMTP, int iPort, String sUserName, String sPassWord ) 
    {
        this._SMTP = sSMTP;
        this._Port = iPort;

        this._Auth = true;
        this._AuthUserName = sUserName;
        this._AuthPassWord = sPassWord;

    }

    /**
     * ���M��`����
     */
    private Session getInitSession()
    {
        Properties oProps = new Properties();
        oProps.put( "mail.smtp.host", _SMTP );
        oProps.put( "mail.smtp.port", String.valueOf( _Port ) );
        oProps.put( "mail.smtp.auth", String.valueOf( _Auth ) );
        oProps.put( "mail.smtp.debug", String.valueOf( _Dubug ) );

        Session oSession = null;
        if ( _Auth == true )
        {
            oProps.put( "mail.smtp.socketFactory.port", String.valueOf( _Port ) );
            oProps.put( "mail.smtp.socketFactory.class", SSL_FACTORY );
            oProps.put( "mail.smtp.socketFactory.fallback", String.valueOf( false ) );

            oSession = Session.getDefaultInstance( oProps, new javax.mail.Authenticator()
            {
                @Override
                protected PasswordAuthentication getPasswordAuthentication() 
                {
                    return ( new PasswordAuthentication( _AuthUserName, _AuthPassWord ) );
                }
            } );
        }
        else 
        {
            oSession = Session.getDefaultInstance( oProps );
        }
        oSession.setDebug( _Dubug );

        return ( oSession );
    }

    /**
     * ���[���A�h���X��`
     * @param sAddress ���[���A�h���X
     * @param sAlias �ʖ�
     * @return ���[���A�h���X�I�u�W�F�N�g
     * @throws AddressException
     */
    private InternetAddress setAddress( String sAddress, String sAlias ) throws AddressException
    {
        try 
        {
            return ( new InternetAddress( sAddress, sAlias, _AddressAliasEncode ) );
        }
        catch ( UnsupportedEncodingException e )
        {
            return ( new InternetAddress( sAddress ) );
        }
    }

    /**
     * ���M�҃��[���A�h���X��`
     * @return
     * @throws AddressException 
     */
    private Address getFromAddress() throws AddressException
    {
        Enumeration<String> Keys = pearFrom.keys();
        if ( Keys.hasMoreElements() )
        {
            String sAddress = String.valueOf( Keys.nextElement());
            String sAlias = String.valueOf( pearFrom.get( sAddress ) );
            return ( setAddress( sAddress, sAlias ) );
        }
        else return ( new InternetAddress() );

    }

    /**
     * ���M�҃��[���A�h���X��`���X�g
     * @return
     * @throws AddressException 
     */
    private Address[] getToAddressList() throws AddressException
    {
        InternetAddress[] listAddress = new InternetAddress[listTo.size()]; 
        Enumeration<String> list = listTo.keys();

        int iIndex = 0; 
        while ( list.hasMoreElements() )
        {
            String sAddress = String.valueOf( list.nextElement() );
            String sAlias = String.valueOf( listTo.get( sAddress ) );
            listAddress[iIndex] = setAddress( sAddress, sAlias );
            iIndex++;
        }

        return ( listAddress );
    }

    /**
     * CC���[���A�h���X��`���X�g
     * @return
     * @throws AddressException 
     */
    private Address[] getCCAddressList() throws AddressException
    {
        InternetAddress[] listAddress = new InternetAddress[listCC.size()]; 
        Enumeration<String> list = listCC.keys();

        int iIndex = 0; 
        while ( list.hasMoreElements() )
        {
            String sAddress = String.valueOf( list.nextElement() );
            String sAlias = String.valueOf( listCC.get( sAddress ) );
            listAddress[iIndex] = setAddress( sAddress, sAlias );
            iIndex++;
        }

        return ( listAddress );
    }

    /**
     * BCC���[���A�h���X��`���X�g
     * @return
     * @throws AddressException 
     */
    private Address[] getBCCAddressList() throws AddressException
    {       
        InternetAddress[] listAddress = new InternetAddress[listBCC.size()]; 
        Enumeration<String> list = listBCC.keys();

        int iIndex = 0; 
        while ( list.hasMoreElements() )
        {
            String sAddress = String.valueOf( list.nextElement() );
            String sAlias = String.valueOf( listBCC.get( sAddress ) );
            listAddress[iIndex] = setAddress( sAddress, sAlias );
            iIndex++;
        }

        return ( listAddress );
    }

    /**
     * �Y�t�t�@�C�������݂��邩�ǂ���
     * @return
     */
    private boolean isExistAttachment()
    {
        return ( listFilePath.size() > 0 );
    }


    /**
     * �{��
     * @return
     * @throws MessagingException
     */
    private MimeBodyPart getTextPart() throws MessagingException
    {
        MimeBodyPart oMbp = new MimeBodyPart();
              oMbp.setText( _Body, _TextEncode );

         return ( oMbp );
    }


    /**
     * �t�@�C���p�[�g���쐬
     * @return
     * @throws MessagingException 
     * @throws IOException 
     */
    private MimeBodyPart[] getFilePart() throws IOException, MessagingException
    { 
            int iCount = listFilePath.size();
           MimeBodyPart[] listMbp = new MimeBodyPart[iCount];
           for ( int i = 0; i < iCount; i++ )
           {
               MimeBodyPart oMbp = new MimeBodyPart();
               // �Y�t����t�@�C�������w��
              FileDataSource fds = new FileDataSource( (String)listFilePath.get( i ) );
                oMbp.setDataHandler( new DataHandler( fds ) );
                oMbp.setFileName( MimeUtility.encodeWord( fds.getName(), _AttachmentFileNameEncode, "B" ) );
                //oMbp.setFileName( MimeUtility.encodeText( fds.getName(), _AttachmentFileNameEncode, "B" ) );
                listMbp[i] = oMbp;

            }
             return ( listMbp );
    }


    /**
     * ���[�����M
     * @return
     * @throws MessagingException 
     * @throws AddressException 
     * @throws SenderException ���[�����M�G���[  
     * @throws IOException 
     */
    public void sendMail() throws AddressException, MessagingException, SenderException, IOException
    {
        //���M�ݒ�
        MimeMessage msg = new MimeMessage( getInitSession() );
        msg.setFrom( getFromAddress() );
        msg.setRecipients( Message.RecipientType.TO, getToAddressList() );
        msg.setRecipients( Message.RecipientType.CC, getCCAddressList() );
        msg.setRecipients( Message.RecipientType.BCC, getBCCAddressList() );
        //���M����
        msg.setSubject( _Subject, _SubjectEncode );
        msg.setSentDate( new Date() );

        Multipart oMp = new MimeMultipart();
        oMp.addBodyPart( getTextPart() );

        //�Y�t�t�@�C��
        if ( isExistAttachment() )
        {
                MimeBodyPart[] aryFilePart = getFilePart();
                int iCount = aryFilePart.length;
                for ( int i = 0; i < iCount; i++ )
                {
                    oMp.addBodyPart( aryFilePart[i] );
                }
            }

        //���M�R���e���c�{��
            msg.setContent( oMp );

            //���M����
            sendMail( msg );
    }


    /**
     * ���[�����M
     * @param msg
     * @throws SenderException
     */
    private void sendMail( MimeMessage msg ) throws SenderException
    {
        try 
        {
            Transport.send( msg );
        }
        catch ( Exception e ) 
        {
            throw ( new SenderException( e ) );
        }
    }
}