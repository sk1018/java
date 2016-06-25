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
     * メール送信エラー
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

    /** エンコード 定義*/
    private final String Encode_ISO2022 = "ISO-2022-JP";

    /** エンコード 定義*/
    private final String Encode_UTF8 = "UTF-8";

    /** SSL */
    private final String SSL_FACTORY = "javax.net.ssl.SSLSocketFactory";

    /** 送信者/別名リスト */
    private Hashtable<String, String> listTo = new Hashtable<String, String>();
    /** CC/別名リスト */
    private Hashtable<String, String> listCC = new Hashtable<String, String>();
    /** BCC/別名リスト */
    private Hashtable<String, String> listBCC = new Hashtable<String, String>();
    /** 送信者/別名 */
    private Hashtable<String, String> pearFrom = new Hashtable<String, String>(1);

    /** 添付ファイル */
    private List<String> listFilePath = new ArrayList<String>();

    /** エンコード */
    private String _AttachmentFileNameEncode = Encode_ISO2022;

    /** エンコード */
    private String _AddressAliasEncode = Encode_ISO2022; 

    /** エンコード */
    private String _SubjectEncode = Encode_ISO2022;

    /** エンコード */
    private String _TextEncode = Encode_ISO2022;

    /** 件名 */
    private String _Subject = "Subject";

    /** 本文 */
    private String _Body = "Body";

    /** SMTPサーバ */
    private String _SMTP = "localhost";

    /** ポート */
    private int _Port = 25;

    /** デバッグ */
    private boolean _Dubug = false;

    /** 認証 */
    private boolean _Auth = false;

    /** 認証ID */
    private String _AuthUserName = "";

    /** 認証パスワード */
    private String _AuthPassWord = "";


    /**
     * 発信者格納
     * @param sFrom 発信者アドレス
     */
    public void setFromAddress( String sFrom )
    {
        pearFrom.clear();
        pearFrom.put( sFrom, "" );
    }

    /**
     * 発信者格納
     * @param sFrom 送信者アドレス
     * @param sAlias 送信者別名
     */
    public void setFromAddress( String sFrom, String sAlias )
    {
        pearFrom.clear();
        pearFrom.put( sFrom, sAlias );
    }

    /**
     * 送信者を格納する
     * @param sTo 送信者アドレス
     * @throws AddressException
     */
    public void addToAddress( String sTo )
    {
        listTo.put( sTo, "" );
    }

    /**
     * 送信者を格納する
     * @param sTo 送信者アドレス
     * @param sAlias 送信者別名
     * @throws AddressException
     */
    public void addToAddress( String sTo, String sAlias )
    {
        listTo.put( sTo, sAlias );
    }

    /**
     * CCを格納する
     * @param sCC CCアドレス
     * @throws AddressException
     */
    public void addCCAddress( String sCC )
    {
        listCC.put( sCC, "" );
    }

    /**
     * CCを格納する
     * @param sCC CCアドレス
     * @param sAlias CC別名
     * @throws AddressException
     */
    public void addCCAddress( String sCC, String sAlias )
    {
        listCC.put( sCC, sAlias );
    }

    /**
     * BCCを格納する
     * @param sBCC BCCアドレス
     * @throws AddressException
     */
    public void addBCCAddress( String sBCC )
    {
        listBCC.put( sBCC, "" );
    }

    /**
     * BCCを格納する
     * @param sBCC BCCアドレス
     * @param sAlias BCC別名
     * @throws AddressException
     */
    public void addBCCAddress( String sBCC, String sAlias )
    {
        listBCC.put( sBCC, sAlias );
    }



    /**
     * 件名を格納する
     * @param sSubject 件名
     */
    public void setSubject ( String sSubject )
    {
        this._Subject = sSubject;

    }

    /**
     * 本文を格納する
     * @param sBody 本文
     */
    public void setBody ( String sBody )
    {
        this._Body = sBody;
    }

    /**
     * 添付ファイルパスリスト
     * @param sFilePath
     */
    public void addAttachment( String sFilePath )
    {
        listFilePath.add( sFilePath );
    }

    /**
     * コンストラクタ
     * デフォルト値
     * SMTP：localhost
     * Port：25
     */
    public MailSender( )
    {}

    /**
     * コンストラクタ
     * デフォルト値
     * Port：25
     */
    public MailSender( String sSMTP )
    {
        this._SMTP = sSMTP;
    }

    /**
     * コンストラクタ
     * @param sSMTP SMTPサーバ
     * @param iPort ポート
     */
    public MailSender( String sSMTP, int iPort ) 
    {
        this._SMTP = sSMTP;
        this._Port = iPort;

    }

    /**
     * コンストラクタ
     * @param sSMTP SMTP
     * @param iPort ポート
     * @param sUserName 認証ID
     * @param sPassWord 認証パスワード
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
     * 送信定義生成
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
     * メールアドレス定義
     * @param sAddress メールアドレス
     * @param sAlias 別名
     * @return メールアドレスオブジェクト
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
     * 発信者メールアドレス定義
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
     * 送信者メールアドレス定義リスト
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
     * CCメールアドレス定義リスト
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
     * BCCメールアドレス定義リスト
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
     * 添付ファイルが存在するかどうか
     * @return
     */
    private boolean isExistAttachment()
    {
        return ( listFilePath.size() > 0 );
    }


    /**
     * 本文
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
     * ファイルパートを作成
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
               // 添付するファイル名を指定
              FileDataSource fds = new FileDataSource( (String)listFilePath.get( i ) );
                oMbp.setDataHandler( new DataHandler( fds ) );
                oMbp.setFileName( MimeUtility.encodeWord( fds.getName(), _AttachmentFileNameEncode, "B" ) );
                //oMbp.setFileName( MimeUtility.encodeText( fds.getName(), _AttachmentFileNameEncode, "B" ) );
                listMbp[i] = oMbp;

            }
             return ( listMbp );
    }


    /**
     * メール送信
     * @return
     * @throws MessagingException 
     * @throws AddressException 
     * @throws SenderException メール送信エラー  
     * @throws IOException 
     */
    public void sendMail() throws AddressException, MessagingException, SenderException, IOException
    {
        //送信設定
        MimeMessage msg = new MimeMessage( getInitSession() );
        msg.setFrom( getFromAddress() );
        msg.setRecipients( Message.RecipientType.TO, getToAddressList() );
        msg.setRecipients( Message.RecipientType.CC, getCCAddressList() );
        msg.setRecipients( Message.RecipientType.BCC, getBCCAddressList() );
        //送信件名
        msg.setSubject( _Subject, _SubjectEncode );
        msg.setSentDate( new Date() );

        Multipart oMp = new MimeMultipart();
        oMp.addBodyPart( getTextPart() );

        //添付ファイル
        if ( isExistAttachment() )
        {
                MimeBodyPart[] aryFilePart = getFilePart();
                int iCount = aryFilePart.length;
                for ( int i = 0; i < iCount; i++ )
                {
                    oMp.addBodyPart( aryFilePart[i] );
                }
            }

        //送信コンテンツ本体
            msg.setContent( oMp );

            //送信処理
            sendMail( msg );
    }


    /**
     * メール送信
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