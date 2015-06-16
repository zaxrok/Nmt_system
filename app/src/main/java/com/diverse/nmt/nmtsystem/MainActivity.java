package com.diverse.nmt.nmtsystem;


import android.content.Context;
import android.content.Intent;
import android.database.Cursor;
import android.media.AudioManager;
import android.media.MediaPlayer;
import android.net.Uri;
import android.os.CountDownTimer;
import android.os.Handler;
import android.os.Message;
import android.os.RemoteException;
import android.os.StrictMode;
import android.provider.ContactsContract;
import android.speech.RecognitionListener;
import android.speech.RecognizerIntent;
import android.speech.SpeechRecognizer;
import android.speech.tts.TextToSpeech;
import android.support.v7.app.ActionBarActivity;
import android.os.Bundle;
import android.telephony.SmsManager;
import android.text.method.ScrollingMovementMethod;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.inputmethod.InputMethodManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import org.apache.http.Header;
import org.apache.http.HttpResponse;
import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.impl.client.DefaultHttpClient;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.io.UnsupportedEncodingException;
import java.net.URI;
import java.net.URLEncoder;
import java.util.ArrayList;
import java.util.Locale;


public class MainActivity extends ActionBarActivity implements TextToSpeech.OnInitListener {

    int aa = 1;
    String  temp;
    String name;
    String smstext;
    String wordgamelist;
    String check;
    String sub2;
    String str2;
    String tagresult;
    String parseresult;
    String[] searchaskurl;
    String[] askanswer;
    int  searchparsenum;
    int  searchcodenum;
    String searchsaved;
    String speakstr;
    String getcookie;
    String getcookie2;
    String phonenumber;
    Handler handler = new Handler();
    ProgressBar mProgCircle;
    TextToSpeech _tts;
    boolean _ttsActive = false;
    Intent i = new Intent(RecognizerIntent.ACTION_RECOGNIZE_SPEECH);
    SpeechRecognizer mRecognizer;
    private MediaPlayer mediaPlayer;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        if(android.os.Build.VERSION.SDK_INT > 9) {
            StrictMode.ThreadPolicy policy = new StrictMode.ThreadPolicy.Builder().permitAll().build();
            StrictMode.setThreadPolicy(policy);
        }




        mProgCircle = (ProgressBar) findViewById(R.id.progressBar);
        mProgCircle.setVisibility(View.INVISIBLE);
        i.putExtra(RecognizerIntent.EXTRA_LANGUAGE_MODEL, RecognizerIntent.LANGUAGE_MODEL_FREE_FORM);
        i.putExtra(RecognizerIntent.EXTRA_CALLING_PACKAGE, getPackageName());
        i.putExtra(RecognizerIntent.EXTRA_LANGUAGE, "ko-KR");
        //i.putExtra(RecognizerIntent.EXTRA_SPEECH_INPUT_COMPLETE_SILENCE_LENGTH_MILLIS, 30000);
        mRecognizer = SpeechRecognizer.createSpeechRecognizer(this);
        mRecognizer.setRecognitionListener(listener);
        handler.postDelayed(new Runnable() {
            public void run() {
               first_ask();
            }
        }, 1000);
        Button button1 = (Button) findViewById(R.id.button2);{
            button1.setOnClickListener(new View.OnClickListener() {
                public void onClick(View v) {
                   first_ask();
                }
            });
        }
        Button button2 = (Button) findViewById(R.id.button);{
            button2.setOnClickListener(new View.OnClickListener() {
                public void onClick(View v) {
                    check = "영";
                    mediaPlayer.stop();
                }
            });
        }
    }
    //여기서 부터 함수_________________________________________________________________________________________________________________________

    //음성인식 함수_____________________________________________________________________________________________________________________
    private RecognitionListener listener = new RecognitionListener() {
        @Override
            public void onRmsChanged(float rmsdB) {
        }
        @Override
        public void onResults(Bundle results) {
            mProgCircle.setVisibility(View.INVISIBLE);
            String key = "";
            key = SpeechRecognizer.RESULTS_RECOGNITION;
            ArrayList<String> mResult = results.getStringArrayList(key);
            askanswer = new String[mResult.size()];
            mResult.toArray(askanswer);
            TextView tv3 = (TextView) findViewById(R.id.textView3);
            tv3.setText("인식결과 : " + askanswer[0]);
            if (check == "영"){
                if (askanswer[0].contains("너")) {
                    first_ask();
                }
                else if (askanswer[0].contains("검색")) {
                    search_ask();
                }
                else if (askanswer[0].contains("뭐야")) {
                    if (askanswer[0].contains("뜻")) {
                        dic_ask();
                    }
                    else{
                        search_ask();
                    }
                }
                else if (askanswer[0].contains("뜻")) {
                    dic_ask();
                }
                else if (askanswer[0].contains("다시")) {
                    first_ask();
                }
                else if (askanswer[0].contains("꺼져")) {
                    android.os.Process.killProcess(android.os.Process.myPid());
                    System.exit(1);
                }
                else if (askanswer[0].contains("듯")) {
                    dic_ask();
                }
                else if (askanswer[0].contains("알려줘")) {
                    search_ask();
                }
                else if (askanswer[0].contains("끝말")) {
                    wordgame_ask();
                }
                else if (askanswer[0].contains("전화")) {
                    phone_ask();
                }
                else if (askanswer[0].contains("통화")) {
                    phone_ask();
                }
                else if (askanswer[0].contains("문자")) {
                    sms_ask();
                }
                else if (askanswer[0].contains("들려")) {
                    music_ask();
                }
                else if (askanswer[0].contains("드")) {
                    dic_ask();
                }
                else if (askanswer[0].contains("영상")) {
                    movie_ask();
                }
                else if (askanswer[0].contains("노래")) {
                    music_ask();
                }
                else if (askanswer[0].contains("차트")) {
                    if (askanswer[0].contains("음악")) {
                        music_chart();
                    }
                    else if (askanswer[0].contains("음원")) {
                        music_chart();
                    }
                }
                else if (askanswer[0].contains("메세지")) {
                    sms_ask();
                }
                else if(askanswer[0].contains("다시")){
                    speak("다시 말해주세요.");
                    handler.postDelayed(new Runnable() {
                        public void run() {
                           first_ask();
                        }
                    }, speakstr.length() * 220);
                }
                else{
                    speak("이해하지못했어요.");
                    handler.postDelayed(new Runnable() {
                        public void run() {
                            mProgCircle.setVisibility(View.VISIBLE);
                            mRecognizer.startListening(i);
                        }
                    }, speakstr.length() * 220);
                }
            }
            //여기서부터는 분류
            if (check == "검색"){
                search_ask();
            }
            if (check == "영화"){
                movie_ask();
            }
            if (check == "전화"){
                phone_ask();
            }
            if (check == "문자"){
                sms_ask();
            }
            if (check == "음악"){
                music_ask();
            }
            if (check == "끝말"){
                wordgame_ask();
            }
            if (check == "사전"){
                dic_ask();
            }
        }
        @Override
        public void onReadyForSpeech(Bundle params) {
        }
        @Override
        public void onPartialResults(Bundle partialResults) {
        }
        @Override
        public void onEvent(int eventType, Bundle params) {
        }
        @Override
        public void onError(int error) {
           mRecognizer.startListening(i);
        }
        @Override
        public void onEndOfSpeech() {
        }
        @Override
        public void onBufferReceived(byte[] buffer) {
        }
        @Override
        public void onBeginningOfSpeech() {
        }
    };
    //http 검색____________________________________________________________________________________________________________________________________
    public void search_ask() {
        TextView tv = (TextView) findViewById(R.id.textView);
        TextView tv2 = (TextView) findViewById(R.id.textView2);
        check = "검색";
        tagdel3(askanswer[0]);
        str2 = "http://m.kin.naver.com/mobile/search/searchList.nhn?cs=utf8&query=" + tagresult;
        if (searchcodenum == 0){
            if (askanswer[0].contains("아니")) {
                searchcodenum = 1;
                tagdel3(searchsaved);
                str2 = "http://m.kin.naver.com/mobile/search/searchList.nhn?cs=utf8&query=" + tagresult;
                searchparsenum = searchparsenum + 1 ;
            }
            if (askanswer[0].contains("어")) {
                searchcodenum = 7;
            }
            if (askanswer[0].contains("네")) {
                searchcodenum = 7;
            }
            if (askanswer[0].contains("응")) {
                searchcodenum = 7;
            }
            if (askanswer[0].contains("다시")) {
                str2 = "";
                searchcodenum = 100;
                check = "영";
                speak("다시검색합니다");
                handler.postDelayed(new Runnable() {
                    public void run() {
                        first_ask();
                    }
                }, speakstr.length() * 220);
            }
            else{
                searchcodenum = 1;
                tagdel3(searchsaved);
                str2 = "http://m.kin.naver.com/mobile/search/searchList.nhn?cs=utf8&query=" + tagresult;
                searchparsenum = searchparsenum + 1 ;
            }
        }
        if (searchcodenum == 5) {
            if (askanswer[0].contains("어")) {
                speak("연결합니다.");
                handler.postDelayed(new Runnable() {
                    public void run() {
                        Intent browserIntent = new Intent(Intent.ACTION_VIEW, Uri.parse(searchaskurl[0]));
                        startActivity(browserIntent);
                    }
                }, speakstr.length() * 210);
            }
            if (askanswer[0].contains("아니")) {
                str2 = "";
                searchcodenum = 100;
                check = "영";
                first_ask();
            }
            else{
                str2 = "";
                searchcodenum = 100;
                check = "영";
                first_ask();
            }
        }
        if (searchcodenum == 1){
            posthttp(str2);
            parsedata(sub2,"<li class=\"lst_divide\"> \n" + "\t\t\t\t\t\t\t<a href=\"/mobile", "\"",searchparsenum);
            posthttp("http://m.kin.naver.com/mobile" + parseresult);
            parsedata(sub2, "<meta name=\"title\" content=\"",": 지식iN\"/>",1);
            tagdel1(parseresult); //태그제거
            tv2.setText("검색어 : " + tagresult);
            speak(tagresult + "을 검색할까요?");
            handler.postDelayed(new Runnable() {
                public void run() {
                    searchcodenum = 0;
                    searchsaved = askanswer[0];
                    mProgCircle.setVisibility(View.VISIBLE);
                    mRecognizer.startListening(i);
                }
            }, speakstr.length() * 210);
        }
        if (searchcodenum == 7){
            parsedata(sub2,"<div class=\"article_content content_lv2\">\n","<div class=\"end_abt\">",1);
            if (parseresult.contains("<a href=\"")) {
                String[] parse1 = (parseresult.split("<a href=\""));
                searchaskurl = parse1[1].split("\" target='_blank'>");
            }
            tagdel2(parseresult); //태그제거
            tv.setMovementMethod(new ScrollingMovementMethod());
            tv.setText(tagresult);
            speak(tagresult);
            handler.postDelayed(new Runnable() {
                public void run() {
                    if(searchaskurl[0] != null){
                        speak("링크가 있습니다. 연결할까요?");
                        handler.postDelayed(new Runnable() {
                            public void run() {
                                searchcodenum = 5;
                                searchsaved = askanswer[0];
                                mProgCircle.setVisibility(View.VISIBLE);
                                mRecognizer.startListening(i);
                            }
                        }, speakstr.length() * 210);
                    }
                    else{
                        first_ask();
                    }
                }
            }, tagresult.length() * 100);
        }
    }
    //전화걸기 함수_________________________________________________________________________________________________________________________________
    public void phone_ask() {
        check = "전화";
        if (searchcodenum == 1){
            tagdel4(askanswer[0]);
            speak(tagresult + "에게 전화 걸까요?");
            handler.postDelayed(new Runnable() {
                public void run() {
                    searchcodenum = 0;
                    searchsaved = askanswer[0];
                    mProgCircle.setVisibility(View.VISIBLE);
                    mRecognizer.startListening(i);
                }
            }, speakstr.length() * 220);
        }
        if (searchcodenum == 0){
            if (askanswer[0].contains("어")) {
                phone_ask_ok();
            }
            else if (askanswer[0].contains("걸어")) {
                phone_ask_ok();
            }
            else if (askanswer[0].contains("걸지마")) {
                phone_ask_no();
            }
            else if (askanswer[0].contains("그러지마")) {
                phone_ask_no();
            }
            else if (askanswer[0].contains("아니")) {
                phone_ask_no();
            }
            else if (askanswer[0].contains("가지마")) {
                phone_ask_no();
            }
            else if (askanswer[0].contains("아")) {
                phone_ask_ok();
            }
            else{
                phone_ask_no();
            }
        }
    }
    public void phone_ask_ok() {
        phonenumber = getPhoneNumber(getApplicationContext(), tagresult);
        if(phonenumber == ""){
            speak("잘못된 번호입니다.");
            handler.postDelayed(new Runnable() {
                public void run() {
                    searchcodenum = 0;
                    searchsaved = askanswer[0];
                    mProgCircle.setVisibility(View.VISIBLE);
                    mRecognizer.startListening(i);
                }
            }, speakstr.length() * 220);
        }
        else{
            callPhone(this, phonenumber);
        }
    }
    public void phone_ask_no() {
        check = "영";
        speak("전화걸지 않습니다.");
        handler.postDelayed(new Runnable() {
            public void run() {
                first_ask();
            }
        }, speakstr.length() * 220);
    }
    //sms 함수___________________________________________________________________________________________________________________________________
    public void sms_ask() {
        check = "문자";
        if (searchcodenum == 1){
            if (askanswer[0].contains("한테")) {
                String[] parse1 = (askanswer[0].split("한테"));
                name = parse1[0];
                if (askanswer[0].contains("라고")) {
                    parsedata(askanswer[0],"한테", "라고",1);
                    smstext = parseresult;
                    speak(name + "에게 " + smstext + "고 보낼까요?");
                    handler.postDelayed(new Runnable() {
                        public void run() {
                            searchcodenum = 3;
                            searchsaved = askanswer[0];
                            mProgCircle.setVisibility(View.VISIBLE);
                            mRecognizer.startListening(i);
                        }
                    }, speakstr.length() * 220);
                }
                else{
                    check = "영";
                    searchparsenum = 1;
                    searchcodenum = 1;
                    speak("뭐라고 보낼까요 다시 명령하세요.");
                    handler.postDelayed(new Runnable() {
                        public void run() {
                            mProgCircle.setVisibility(View.VISIBLE);
                            mRecognizer.startListening(i);
                        }
                    }, speakstr.length() * 220);
                }
            }
            else{
                check = "영";
                searchparsenum = 1;
                searchcodenum = 1;
                speak("누구한테 보낼까요 다시 명령하세요.");
                handler.postDelayed(new Runnable() {
                    public void run() {
                        mProgCircle.setVisibility(View.VISIBLE);
                        mRecognizer.startListening(i);
                    }
                }, speakstr.length() * 220);
            }
        }
        if (searchcodenum == 3){
            if (askanswer[0].contains("어")) {
                sms_ask_ok();
            }
            else if (askanswer[0].contains("보내지마")) {
                sms_ask_no();
            }
            else if (askanswer[0].contains("보내")) {
                sms_ask_ok();
            }
            else if (askanswer[0].contains("아니")) {
                sms_ask_no();
            }
            else{
                sms_ask_no();
            }
        }
    }
    public void sms_ask_ok() {
        phonenumber = getPhoneNumber(getApplicationContext(), name);
        tagdel4(phonenumber);
        try {
            SmsManager smsManager = SmsManager.getDefault();
            smsManager.sendTextMessage(tagresult, null, smstext, null, null);
            speak("성공적으로 보냈습니다.");
            check = "영";
            handler.postDelayed(new Runnable() {
                public void run() {
                    first_ask();
                }
            }, speakstr.length() * 220);
        } catch (Exception e) {
            speak("전송에 실패했습니다.");
            handler.postDelayed(new Runnable() {
                public void run() {
                    first_ask();
                }
            }, speakstr.length() * 220);
        }
    }
    public void sms_ask_no() {
        check = "영";
        speak("문자 보내지 않습니다.");
        handler.postDelayed(new Runnable() {
            public void run() {
                first_ask();
            }
        }, speakstr.length() * 220);
    }
    //음악 함수_____________________________________________________________________________________________________________________________________________
    public void music_ask() {
        check = "음악";
        if (searchcodenum == 1){
            tagdel6(askanswer[0]);
            posthttp("http://app.genie.co.kr/Iv3/Search/f_Search_Song.asp?query=" + tagresult + "&pagesize=200");
            sub2 = sub2.toString();
            sub2 = sub2.replace("<span class=\\\"t_point\\\">","");
            sub2 = sub2.replace("<\\/span>","");
            parsedata(sub2,"SONG_NAME\":\"","\"",1 ) ;
            speak(parseresult + "을 재생합니다");
            handler.postDelayed(new Runnable() {
                public void run() {
                    posthttp("http://app.genie.co.kr/Iv3/Search/f_Search_Song.asp?query=" + tagresult + "&pagesize=200");
                    sub2 = sub2.toString();
                    sub2 = sub2.replace("<span class=\\\"t_point\\\">","");
                    sub2 = sub2.replace("<\\/span>","");
                    parsedata(sub2,"SONG_ID\":\"","\"",1 ) ;
                    posthttp("https://app.genie.co.kr/Iv3/Player/j_AppStmInfo_V2.asp?xgnm=" +  parseresult + "&uxtk=30516014131527.46&unm=305160141&bitrate=" + "192");
                    parsedata(sub2,"STREAMING_MP3_URL\": \"","\",",1);
                    try {
                        parseresult = java.net.URLDecoder.decode(parseresult, "UTF-8");
                    } catch (UnsupportedEncodingException e) {
                        e.printStackTrace();
                    }
                    try {
                        playAudio(parseresult);
                        first_ask();
                    } catch (Exception e) {
                        e.printStackTrace();
                    }
                }
            }, speakstr.length() * 210);
        }
    }
    //영화 함수__________________________________________________________________________________________________________________________________________
    public void movie_ask() {
        check = "영화";
        if (searchcodenum == 1){
            tagdel7(askanswer[0]);
            try {
                tagresult = URLEncoder.encode(tagresult, "euc-kr");
            } catch (UnsupportedEncodingException e) {
                e.printStackTrace();
            }
            posthttp2("https://www.filenori.com/nori/search.jsp?category=&o_category=&linesperpage=20&cate_ds=0&pd=&s_sub=&ps=&wordtype2=21&wordtype=21&cpr=&chk_disabled=&search_word=&qry=" + tagresult + "&search.x=36&search.y=20&rc=20"
                    , "https://www.filenori.com/nori/search.jsp?category=&o_category=&linesperpage=20&cate_ds=0&pd=&s_sub=&ps=&wordtype2=21&wordtype=21&cpr=&chk_disabled=&search_word=&qry=kpop+star&search.x=36&search.y=20&rc=20"
                    , "NSHcookie=200907221b0a72d26c6f0003; JSESSIONID=1B14A1874A9FEC52A3984794C178DBE1");
            sub2 = sub2.replace("<img src='/img/icon_19.gif' border='0' align='absmiddle'>","");
            sub2 = sub2.replace("&gt;gt;","");
            parsedata(sub2,"<td height=\"27\" width=\"483\" title=\"","\" style=\"padding:2px 0 0 14px",1);
            talk(parseresult + "을 재생합니다");
            parsedata(sub2,"a href=\"javascript:openDnWin\\(",", 'N'",1);
            posthttp2("http://filenori.com/mobile/mobile_list.jsp","","");
            getcookie2 = getcookie;
            posthttp2("http://filenori.com/mobile/mobile_list.jsp","",getcookie);
            getcookie2 = getcookie2 + "; " + getcookie;
            posthttp2("http://filenori.com/mobile/include/db_login.jsp?partner_gu=&ret_url=&mobile_key=&partner_cd=&chk_app=&userid=v3qurwls&passwd=aaaa1111&al_cookie_save=Y&gopage=%2Findex.jsp%3Fpage%3Dbbs","",getcookie2);
            getcookie2 = getcookie2 + "; " + getcookie;
            posthttp2("http://filenori.com/mobile/myList/contents_MyList_view.jsp?id=" + parseresult ,"",getcookie2);
            String[] data3 = sub2.split("openPlayView\\('");
            String[] data4 = data3[1].split("'");
             posthttp2("http://filenori.com/mobile/content_getAddr.jsp?seq=" + data4[0] + "&id=" + parseresult,"",getcookie2);
            handler.postDelayed(new Runnable() {
                public void run() {
                    Intent browserIntent = new Intent(Intent.ACTION_VIEW, Uri.parse(sub2));
                    startActivity(browserIntent);
                }
            }, 2000);
        }
    }
    //끝말잇기_______________________________________________________________________________________________________________________________________
        public void wordgame_ask() {
        check = "끝말";
        if(searchcodenum == 1){
            speak("먼저 시작하세요");
            handler.postDelayed(new Runnable() {
                public void run() {
                    temp = "1";
                    searchcodenum = 2;
                    mProgCircle.setVisibility(View.VISIBLE);
                    mRecognizer.startListening(i);
                }
            }, speakstr.length() * 220);
        }
        if(searchcodenum == 2){
            if (temp == "1"){
            }
            else if (temp == "2") {
                String a = wordgamelist.substring(wordgamelist.length() - 1,wordgamelist.length());
                String b = askanswer[0].substring(0,1);
                if (!a.equals(b)) {
                speak("틀렸어요 제가 이겼네요.");
                searchcodenum = 3;
                handler.postDelayed(new Runnable() {
                        public void run() {
                            first_ask();
                            }
                }, speakstr.length() * 220);
                }
            }
            if (searchcodenum == 2) {
                posthttp("http://dic.naver.com/search.nhn?query=" + askanswer[0]);
               if (sub2.contains("검색결과가")) {
                   speak("없는 단어네요. 제가 이겼습니다.");
                   handler.postDelayed(new Runnable() {
                        public void run() {
                            first_ask();
                        }
                   }, speakstr.length() * 220);
               } else {
                   temp = "2";
                   askanswer[0] = askanswer[0].substring(askanswer[0].length() - 1);
                   posthttp("http://dic.naver.com/search.nhn?query=" + askanswer[0] + "로시작하는단어");
                   parsedata(sub2, "docid=", "\"", 1);
                   posthttp("http://krdic.naver.com/detail.nhn?docid=" + parseresult);
                   parsedata(sub2, "var dnb_query = \"", "\"", 1);
                   speak(parseresult);
                   wordgamelist = parseresult;
                   handler.postDelayed(new Runnable() {
                       public void run() {
                           mProgCircle.setVisibility(View.VISIBLE);
                           mRecognizer.startListening(i);
                       }
                   }, 3000);
               }
            }
        }
    }
    //음악 차트_____________________________________________________________________________________________________________________________________________
    public void music_chart() {
        posthttp("https://app.genie.co.kr/Iv3/SongList/j_RealTimeRankSongList.asp?svc=IV&unm=&pg=1&pgSize=100&apvn=30602&ditc=&uxtk=&uip=192.168.1.3&mts=Y");
        sub2 = sub2.replace("<span class=\\\"t_point\\\">","");
        sub2 = sub2.replace("<\\/span>","");
        sub2 = sub2.replace("%28", "");
        sub2 = sub2.replace("%29" ,"");
        sub2 = sub2.replace("%2C" ,"");
        sub2 = sub2.replace("%26" ,"");
        sub2 = sub2.replace("%29" ,"");

        parsedata(sub2,"SONG_NAME\":\"","\",",aa);
        temp = parseresult;
        parsedata(sub2,"ARTIST_NAME\":\"","\",",aa);
        speak(aa + "위 " + temp + " , " + parseresult);
        handler.postDelayed(new Runnable() {
            public void run() {
                aa++;
                parsedata(sub2,"SONG_NAME\":\"","\",",aa);
                temp = parseresult;
                parsedata(sub2,"ARTIST_NAME\":\"","\",",aa);
                speak(aa + "위 " + temp + " , " + parseresult);
                handler.postDelayed(new Runnable() {
                    public void run() {
                        aa++;
                        parsedata(sub2,"SONG_NAME\":\"","\",",aa);
                        temp = parseresult;
                        parsedata(sub2,"ARTIST_NAME\":\"","\",",aa);
                        speak(aa + "위 " + temp + " , " + parseresult);
                        handler.postDelayed(new Runnable() {
                            public void run() {
                                aa++;
                                parsedata(sub2,"SONG_NAME\":\"","\",",aa);
                                temp = parseresult;
                                parsedata(sub2,"ARTIST_NAME\":\"","\",",aa);
                                speak(aa + "위 " + temp + " , " + parseresult);
                                handler.postDelayed(new Runnable() {
                                    public void run() {
                                        aa++;
                                        parsedata(sub2,"SONG_NAME\":\"","\",",aa);
                                        temp = parseresult;
                                        parsedata(sub2,"ARTIST_NAME\":\"","\",",aa);
                                        speak(aa + "위 " + temp + " , " + parseresult);
                                        handler.postDelayed(new Runnable() {
                                            public void run() {
                                                first_ask();
                                            }
                                        }, speakstr.length() * 180);
                                    }
                                }, speakstr.length() * 180);
                            }
                        }, speakstr.length() * 180);
                    }
                }, speakstr.length() * 180);
            }
        }, speakstr.length() * 180);
    }
    //사전 검색_____________________________________________________________________________________________________________________________________________
    public void dic_ask() {
        check = "사전";
        tagdel8(askanswer[0]);
        //speak(tagresult + "의 뜻을 검색합니다.");
        posthttp("http://translate.naver.com/translate.dic?query="  + tagresult + "&srcLang=en&tarLang=ko");
        //talk(sub2);
        if (sub2.contains("\": \"")) {
            parsedata(sub2,"\": \"","\"",1);
            speak(parseresult + " 라는 뜻입니다.");
            handler.postDelayed(new Runnable() {
                public void run() {
                    first_ask();
                }
            }, speakstr.length() * 300);
        }
        else{
            speak("없는단어입니다. 다시질문해주세요.");
            handler.postDelayed(new Runnable() {
                public void run() {
                    first_ask();
                }
            }, speakstr.length() * 220);
        }

    }








































    //에코제거____________________________________________________________________________________________________________________________

    //음성인식 함수2____________________________________________________________________________________________________________________________
    @Override
    public void onPause() {
        super.onPause();
        try {
            if (_tts != null) {
                _tts.stop();
                _ttsActive = false;
            }
        } catch (Exception e) {
        }
    }
    @Override
    public void onResume() {
        super.onResume();
        _tts = new TextToSpeech(getApplicationContext(), this);
    }
    @Override
    public void onDestroy() {
        super.onDestroy();
        try {
            if (_tts != null) {
                _tts.shutdown();
                _tts = null;
            }
        } catch (Exception e) {
        }
    }
    public void onInit(int status) {
    }
    @Override
    protected void onStop()
    {
        super.onStop();

        if(_tts != null){
            _tts.shutdown();
        }
    }
    //처음질문 함수__________________________________________________________________________________________________________________________
    public void first_ask() {
        check = "영";
        searchparsenum = 1;
        searchcodenum = 1;
        speak("명령해주세요.");
        handler.postDelayed(new Runnable() {
            public void run() {
                mProgCircle.setVisibility(View.VISIBLE);
                mRecognizer.startListening(i);
            }
        }, speakstr.length() * 250);
    }

    //말하기 함수____________________________________________________________________________________________________________________________________
    public void speak(String sstr) {
        Toast.makeText(getApplicationContext(), sstr, Toast.LENGTH_LONG).show();
        _tts.setLanguage(Locale.KOREA);
        _ttsActive = true;
        speakstr = sstr;
        _tts.speak(speakstr, TextToSpeech.QUEUE_FLUSH, null);
    }
    //토스트 함수____________________________________________________________________________________________________________________________________
    public void talk(String sstr) {
        Toast.makeText(getApplicationContext(), sstr, Toast.LENGTH_LONG).show();
    }
    //파싱 데이터____________________________________________________________________________________________________________________________________
    public void parsedata(String sstr1 , String sstr2, String sstr3, int sstr4) {
        String[] parse1 = sstr1.split(sstr2);
        String[] parse2 =  parse1[sstr4].split(sstr3);
        parseresult = parse2[0];
    }
    //POSTHTTP____________________________________________________________________________________________________________________________________
    public void posthttp(String sstr1) {
        try {
            HttpClient client = new DefaultHttpClient();
            HttpGet post = new HttpGet();
            post.setURI(new URI(sstr1));
            HttpResponse resp = client.execute(post);
            BufferedReader br = new BufferedReader(new InputStreamReader(resp.getEntity().getContent()));
            String str = null;
            StringBuilder sb = new StringBuilder();
            while ((str = br.readLine()) != null) {
                sb.append(str).append("\n");
            }
            br.close();
            sub2 = sb.toString();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
    //테그제거1____________________________________________________________________________________________________________________________________
    public void tagdel1(String sstr) {
        sstr = sstr.replace("@", "");
        sstr = sstr.replace("!", "");
        sstr = sstr.replace("~", "");
        sstr = sstr.replace("&amp;", "&");
        tagresult =  sstr;
    }
    //테그제거2____________________________________________________________________________________________________________________________________
    public void tagdel2(String sstr) {
        sstr = sstr.replace("\n", "");
        sstr = sstr.replace("\t", "");
        sstr = sstr.replace("&#160;", "");
        sstr = sstr.replace("&lt;", "");
        sstr = sstr.replace("..", ".");
        sstr = sstr.replace("&gt;", "");
        sstr = sstr.replace("<!-- 첨부파일 영역 -->", "");
        sstr = sstr.replace("<!-- // 첨부파일 영역 -->", "");
        sstr = sstr.replace("&nbsp;", "");
        sstr = sstr.replace("정말 많은 도움 되었습니다.", "");
        sstr = sstr.replace("정말 친절한 답변 감사드려요~", "");
        sstr = sstr.replace("반갑습니다", "");
        sstr = sstr.replace("답변 덕분에 많이 알아갑니다.", "");
        sstr = sstr.replace("나중에 또 질문해도 답변해 주실거죠?", "");
        sstr = sstr.replace("질문자인사", "");
        sstr = sstr.replace("~", "");
        sstr = sstr.replace("&amp;", "");
        sstr = sstr.replace("&#46468;", "");
        sstr = sstr.replace("요", "요.");
        sstr = sstr.replace("안녕하세요.", "");
        sstr = sstr.replace("안녕하세요", "");
        sstr = sstr.replace("요..", "요.");
        sstr = sstr.replace("ㅎ", "");
        sstr = sstr.replace("ㅋ", "");
        sstr = sstr.replace("니다", "니다.");
        sstr = sstr.replace("니다..", "니다.");
        sstr = sstr.replace("채택부탁드립니다", "");
        sstr = sstr.replace(".", ".\n");
        sstr = sstr.replaceAll("<[^>]*>", "");
        sstr = sstr.replaceAll("\\\\<.*?\\\\>", "");
        sstr = sstr.replaceAll("<(/)?([a-zA-Z]*)(\\\\s[a-zA-Z]*=[^>]*)?(\\\\s)*(/)?>", "");
        tagresult =  sstr;
    }
    //테그제거3____________________________________________________________________________________________________________________________________
    public void tagdel3(String sstr) {
        sstr = sstr.replace(" ", "+");
        sstr = sstr.replace("검색해봐", "");
        sstr = sstr.replace("검색해", "");
        sstr = sstr.replace("검색", "");
        sstr = sstr.replace("대해", "");
        sstr = sstr.replace("검색해줘", "");
        sstr = sstr.replace("에+대해", "");
        sstr = sstr.replace("이+뭐야", "");
        sstr = sstr.replace("뭐야", "");
        sstr = sstr.replace("알려줘", "");
        sstr = sstr.replace("봐", "");
        tagresult =  sstr;
    }
    //테그제거4____________________________________________________________________________________________________________________________________
    public void tagdel4(String sstr) {
        sstr = sstr.replace("한테", "");
        sstr = sstr.replace("해", "");
        sstr = sstr.replace(" ", "");
        sstr = sstr.replace("-", "");
        sstr = sstr.replace("전화", "");
        sstr = sstr.replace("걸어", "");
        sstr = sstr.replace("걸어봐", "");
        sstr = sstr.replace("해봐", "");
        sstr = sstr.replace("전화해", "");
        sstr = sstr.replace("해줘", "");
        sstr = sstr.replace("에게", "");
        sstr = sstr.replace("하고", "");
        sstr = sstr.replace("싶어", "");
        sstr = sstr.replace("통화", "");
        sstr = sstr.replace("연결", "");
        tagresult =  sstr;
    }
    //테그제거5____________________________________________________________________________________________________________________________________
    public void tagdel5(String sstr) {
        sstr = sstr.replace("한테", "");
        sstr = sstr.replace(" ", "");
        sstr = sstr.replace("-", "");
        sstr = sstr.replace("문자해", "");
        sstr = sstr.replace("문자", "");
        sstr = sstr.replace("보내", "");
        sstr = sstr.replace("에게", "");
        tagresult =  sstr;
    }
    //테그제거6____________________________________________________________________________________________________________________________________
    public void tagdel6(String sstr) {
        sstr = sstr.replace("노래", "");
        sstr = sstr.replace(" ", "");
        sstr = sstr.replace("", "");
        sstr = sstr.replace("들려줘", "");
        sstr = sstr.replace("곡", "");
        tagresult =  sstr;
    }
    //테그제거7____________________________________________________________________________________________________________________________________
    public void tagdel7(String sstr) {
        sstr = sstr.replace("영상", "");
        sstr = sstr.replace(" ", "");
        sstr = sstr.replace("-", "");
        sstr = sstr.replace("재생", "");
        sstr = sstr.replace("해줘", "");
        sstr = sstr.replace("해봐", "");
        sstr = sstr.replace("라는", "");
        tagresult =  sstr;
    }
    //테그제거8____________________________________________________________________________________________________________________________________
    public void tagdel8(String sstr) {
        sstr = sstr.replace("의", "");
        sstr = sstr.replace("듯", "");
        sstr = sstr.replace("드", "");
        sstr = sstr.replace(" ", "");
        sstr = sstr.replace("뜻이", "");
        sstr = sstr.replace("뜻", "");
        sstr = sstr.replace("뭐야", "");
        sstr = sstr.replace("모야", "");
        sstr = sstr.replace("좀", "");
        sstr = sstr.replace("무슨", "");
        sstr = sstr.replace("이야", "");
        sstr = sstr.replace("이아", "");
        tagresult =  sstr;
    }
    //전화걸기함수-------------------------------------------------------------------------------------------------------------------------------------
    private boolean callPhone(Context context, String destination)
    {
        Intent intCall = new Intent(Intent.ACTION_CALL, Uri.parse("tel:" + destination));
        intCall.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
        context.startActivity(intCall);
        return true;
    }
    //오디오재생함수-------------------------------------------------------------------------------------------------------------------------------------
    private void playAudio(String url) throws Exception
    {
        killMediaPlayer();
        mediaPlayer = new MediaPlayer();
        mediaPlayer.setDataSource(url);
        mediaPlayer.prepare();
        mediaPlayer.start();
    }
    private void killMediaPlayer() {
        if(mediaPlayer!=null) {
            try {
                mediaPlayer.release();
            }
            catch(Exception e) {
                e.printStackTrace();
            }
        }
    }
    //posthttp2-------------------------------------------------------------------------------------------------------------------------------------
    public void posthttp2(String sstr1 ,String sstr2,String sstr3) {
        try {
            HttpClient client = new DefaultHttpClient();
            HttpPost post = new HttpPost();
            post.setURI(new URI(sstr1));
            post.addHeader("Referer", sstr2);
            post.addHeader("Cookie", sstr3);
            post.addHeader("Content-Type", "application/x-www-form-urlencoded; charset=UTF-8");
            post.addHeader("User-Agent", "Mozilla/5.0 (iPhone; CPU iPhone OS 7_0 like Mac OS X; en-us) AppleWebKit/537.51.1 (KHTML, like Gecko) Version/7.0 Mobile/11A465 Safari/9537.53");
            HttpResponse resp = client.execute(post);
            BufferedReader br = new BufferedReader(new InputStreamReader(resp.getEntity().getContent(), "euc-kr"));
            String str = null;
            StringBuilder sb = new StringBuilder();
            while ((str = br.readLine()) != null) {
                sb.append(str).append("\n");
            }
            br.close();
            sub2 = sb.toString();
            Header[] headers = resp.getHeaders("Set-Cookie");
            for (Header h : headers) {
                getcookie = h.getValue().toString();
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
    //연락처 가져오기-------------------------------------------------------------------------------------------------------------------------------------
    private String getPhoneNumber(Context context, String strName)
    {
        Cursor phoneCursor = null;
        String strReturn = "";
        try
        {
            Uri uContactsUri = ContactsContract.CommonDataKinds.Phone.CONTENT_URI;
            String strProjection = ContactsContract.CommonDataKinds.Phone.DISPLAY_NAME;
            phoneCursor = context.getContentResolver().query(uContactsUri,
                    null, null, null, strProjection);
            phoneCursor.moveToFirst();
            String name = "";
            String number = "";
            String email = "";
            int nameColumn = phoneCursor.getColumnIndex(ContactsContract.CommonDataKinds.Phone.DISPLAY_NAME);
            int numberColumn = phoneCursor.getColumnIndex(ContactsContract.CommonDataKinds.Phone.NUMBER);
            int NumberTypeColumn = phoneCursor.getColumnIndex(ContactsContract.CommonDataKinds.Phone.TYPE);
            while (!phoneCursor.isAfterLast() && strReturn.equals(""))
            {
                name = phoneCursor.getString(nameColumn);
                number = phoneCursor.getString(numberColumn);
                int numberType = Integer.valueOf(phoneCursor.getString(NumberTypeColumn));
                Cursor emailCursor = null;
                try
                {
                    emailCursor = context.getContentResolver()
                            .query(ContactsContract.CommonDataKinds.Email.CONTENT_URI,
                                    new String[] { ContactsContract.CommonDataKinds.Email.DATA },
                                    "DISPLAY_NAME" + "='" + name + "'", null, null);
                    while (emailCursor.moveToNext())
                    {email = emailCursor.getString(emailCursor.getColumnIndex(ContactsContract.CommonDataKinds.Email.DATA));
                    }
                }
                catch (Exception e)
                {
                    first_ask();
                }
                finally
                {if (emailCursor != null)
                { emailCursor.close();
                    emailCursor = null;}
                }
                if(name.equals(strName))
                {strReturn = number;
                }
                name = "";
                number = "";
                email = "";
                phoneCursor.moveToNext();
            }
        }
        catch (Exception e)
        {
        }
        finally
        {
            if (phoneCursor != null)
            {
                phoneCursor.close();
                phoneCursor = null;
            }
        }
        return strReturn;
    }
    //기존 함수____________________________________________________________________________________________________________________________________
    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_main, menu);
        return true;
    }
    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();
        //noinspection SimplifiableIfStatement
        if (id == R.id.action_settings) {
            return true;
        }
        return super.onOptionsItemSelected(item);
    }
  //함수 끝_________________________________________________________________________________________________________________________________________
}