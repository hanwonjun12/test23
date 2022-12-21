package com.example.galpi.book;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.UnsupportedEncodingException;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.net.URLEncoder;
import java.util.ArrayList;


    public class naverApi {   // 내이버 검색 API 모듈

         private String clientId = "GspcVqJWT8jmXn4_dzcy"; //애플리케이션 클라이언트 아이디값"
         private String clientSecret = "i7eva5ILDC"; //애플리케이션 클라이언트 시크릿값"
        public ArrayList<Book> B_List=new ArrayList<Book>(); //  책 목록 리스트를 반환할 변수

        //main(): 하위 모듈들을 합쳐서 전체적인 결과를 실행한다
        //int start : naver에서 제공하는 API 는 검색한 항목들의 값을 가져올떄 시작점을 설정 할수 있다 .
        // 이를 설정하기위해 받아오는 매개변수이다
        //int display : start 매개변수와 마찬가지로 검색하여 데이터를 가져올때 몇가지 항목들을 가져올 것인지
        // 설정하는 매개변수이다
        public  void main(String search_text,int start,int display) {

            String text = null;
            try {
                text = URLEncoder.encode(search_text, "UTF-8");
                // url 에 사용하기 위해서는 한글은 utf-8 구조로 변경되어야 하기 때문에 인코딩 해줍니다
            } catch (UnsupportedEncodingException e) {
                throw new RuntimeException("검색어 인코딩 실패",e);
                //예외 반환
            }

            String apiURL = "https://openapi.naver.com/v1/search/book?query=" + text;
            //apiURL : 네이버 주소에 검색어값을 인코딩한 값을 추가하여 통신할 주소를 저장합니다
            apiURL+="&start="+start; //  매개변수
            apiURL+="&display="+display; // 매개변수

            String responseBody = get(apiURL); //
            parseData(responseBody);

        }

        private  String get(String apiUrl){
            HttpURLConnection con; // java 에서 http통신에 관련한 것들이 정의되어 있는 클래스입니다
            try {
                URL url = new URL(apiUrl); // URL 객체 생성
                con=(HttpURLConnection) url.openConnection(); //초기화
            } catch (MalformedURLException e) { // URl 이 잘못됏을때의 에외
                throw new RuntimeException("API URL이 잘못되었습니다. : " + apiUrl, e);
            } catch (IOException e) { // 나머지 예외 발생
                throw new RuntimeException("연결이 실패했습니다. : " + apiUrl, e);
            }


            try {
                con.setRequestMethod("GET"); // request 방식을 "get" 으로 설정합니다
                con.setRequestProperty("X-Naver-Client-Id", clientId); // 네이버 API 에 등록한 계정의 정보입니다
                con.setRequestProperty("X-Naver-Client-Secret", clientSecret);// 네이버 API 에 등록한 계정의 정보입니다

                if (con.getResponseCode() == 200) { //http 연결이 정상 연결 되었다면 200을 반환합니다
                    return readBody(con.getInputStream()); }// getInputstream() : 실제내용을 읽기위하여 사용용
                else {
                    return readBody(con.getErrorStream()); // 에러값을 반환합니다
                }

            } catch (IOException e) {
                throw new RuntimeException("API 요청과 응답 실패", e);
            } finally {
                con.disconnect(); //httpURLConnection 과 url 연결 해제
            }
        }


        private  String readBody(InputStream body){
            // 스트림 경로에 있는 데이터를 버퍼리더에 불러오기위해 사용합니다
            InputStreamReader streamReader = new InputStreamReader(body);
            // InputStream 형식을 inputStreamReader 형태로 변환 합니다

            try (BufferedReader lineReader = new BufferedReader(streamReader)) {

                StringBuilder responseBody = new StringBuilder();
                String line;
                while ((line = lineReader.readLine()) != null) {
                    responseBody.append(line);
                }
                // BufferReader 객체에있는 RUN()통해 가져온 값을 stringbuilder 객체에 하나씩 저장해줍니다
                return responseBody.toString();
            }
            catch (IOException e) {
                throw new RuntimeException("API 응답을 읽는데 실패했습니다.", e);
            }
        }
        String charsToRemove = ",_!?";

        private void parseData(String responseBody) {
            //responsebody :
            // get()를 통해 http연결 , 연결상태 확인 ,실제내용 가져옴
            //readBody() inputstreamreader 의 데이터들을 stringbuilder 클래스에 저장후 string 형식으로 반환
            String title;
            JSONObject jsonObject = null;

            System.out.println(responseBody);
            try {
                jsonObject = new JSONObject(responseBody);
                //Json 객체 생성
                JSONArray jsonArray = jsonObject.getJSONArray("items");
                // "item"이름을 가지는 json 파일들을 json배열에 저장합니다

                for (int i = 0; i < jsonArray.length(); i++) {
                    //item 객체를 통해 가져온 항목들 각각에대하여 데이터를 Book class에 저장합니다
                    Book book=new Book();
                    JSONObject item = jsonArray.getJSONObject(i);
                    book.title = item.getString("title");

                   String[] temp=chgStr.foo(book.title);

                         /*  book.title.replace(","," ")
                           .replace(".","")
                           .replace("{","")

                           .replace("}","")
                           .replace("(","")
                           .replace(")","")
                           .replace("@","")
                           .replace("!","")
                           .replace("?","")
                           .replace("#","")
                           .replace("/","")
                           .replace("%","")
                           .replace("^","")
                           .replace(":","")
                           .split(" ");
*/

                   for(String s:temp){

                        if(s.length()>1&&!book.keyword_title.contains(s))
                        book.keyword_title.add(s);
                   }
                   System.out.println(book.keyword_title);

                    book.author = item.getString("author");
                    book.link = item.getString("link");
                    book.image = item.getString("image");
                    book.discount = item.getString("discount");
                    book.publisher = item.getString("publisher");
                    book.pubdate=item.getString("pubdate");
                    book.isbn=item.getString("isbn");
                    book.description= item.getString("description");
                    B_List.add(book);
                }

            } catch (JSONException e) {
                e.printStackTrace();
            }

        }


    }

