import kr.dogfoot.hwplib.object.HWPFile;
import kr.dogfoot.hwplib.reader.HWPReader;
import kr.dogfoot.hwplib.tool.textextractor.TextExtractMethod;
import kr.dogfoot.hwplib.tool.textextractor.TextExtractor;
import java.io.*;
import java.net.*;
import java.util.ArrayList;
import java.util.Scanner;
import java.util.concurrent.TimeUnit;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class Main {

    final static int bufferSize = 1024;

    public static void main(String[] args) throws IOException {

        Scanner scanner = new Scanner(System.in);

        while(true){
            System.out.println("경제 관련 키워드를 입력하면 키워드와 연관된 매일경제시험 모의고사문제들과 답을 볼 수 있습니다.");
            System.out.println("모의고사문제를 인터넷에서 다운받아야 하기 때문에 인터넷에 '꼭' 연결되어 있어야 합니다.");
            System.out.print("시작하시겠습니까? (Y / N)  : ");
            String answer = scanner.nextLine();

            if(answer.equals("Y") || answer.equals("y") || answer.equals("Yes") || answer.equals("yes") || answer.equals("YES")){
                System.out.println("*****시작*****");
                break;
            }else if((answer.equals("N") || answer.equals("n") || answer.equals("No") || answer.equals("no") || answer.equals("NO"))){
                quit();
            }else{
                System.out.println("정확하게 입력해 주십시오");
                continue;
            }
        }

        String fromFile = "http://cfile232.uf.daum.net/attach/998C68385BFBECBD154210";
        String localFileName = "매경테스트 기출문제집.DEA.hwp";
        String downloadDir = System.getProperty("user.home") + "/Desktop";

        fileUrlReadAndDownload(fromFile,localFileName,downloadDir);
        ArrayList[] lists = saveProblemAndAnswer(downloadDir);
        ArrayList questions = lists[0];
        ArrayList answers = lists[1];

        loop1 : while(true){
            String input = readInput();
            ArrayList<Integer> answerIndex = printQuestions(input, questions);
            if(answerIndex.size() == 0 ){
                System.out.println("키워드에 해당하는 문제가 없습니다.");
                continue;
            }

            loop2: while(true){
                System.out.println("**(만약 종료를 원하시면 q나 quit을 입력하십시오)**");
                System.out.print("정답 체크하시겠습니까? (Y/N) : ");
                String choice = scanner.nextLine();

                if(choice.equals("Y") || choice.equals("y") || choice.equals("yes") || choice.equals("YES") || choice.equals("Yes")){
                    printAnswers(answerIndex, answers);
                    break loop2;
                }else if(choice.equals("N") || choice.equals("n") || choice.equals("no") || choice.equals("NO") || choice.equals("No")){
                    System.out.println("좀 더 있다가 답을 체크하겠습니다.");
                    try {
                        TimeUnit.SECONDS.sleep(5);
                    } catch (InterruptedException e) {
                        quit();
                    }
                    continue;
                }else if(choice.equals("Q") || choice.equals("q") || choice.equals("quit") || choice.equals("QUIT") || choice.equals("Quit")){
                    quit();
                }else{
                    System.out.println("정확하게 입력해 주십시오");
                    continue;
                }
            }

            while(true){
                System.out.println("**(만약 종료를 원하시면 q나 quit을 입력하십시오)**");
                System.out.print("새로운 keyword를 입력하시겠습니까? (Y/N) : ");
                String choice = scanner.nextLine();
                if(choice.equals("Y") || choice.equals("y") || choice.equals("yes") || choice.equals("YES") || choice.equals("Yes")){
                    continue loop1;
                }else if(choice.equals("N") || choice.equals("n") || choice.equals("no") || choice.equals("NO") || choice.equals("No")){
                    quit();
                }else{
                    System.out.println("정확하게 입력해 주십시오");
                    continue;
                }
            }
        }
    }

    public static void quit() {
        System.out.println("종료하겠습니다.");
        System.exit(0);
    }

    public static void printAnswers(ArrayList<Integer> answerIndex, ArrayList<String> answers) {

        for(int i = 0; i < answerIndex.size(); i++){
            System.out.println(answers.get(answerIndex.get(i)));
        }

    }

    public static ArrayList<Integer> printQuestions(String input, ArrayList questions) {

        int j = 1;
        ArrayList<Integer> answerIndex = new ArrayList<Integer>();

        for(int i = 0; i < questions.size(); i++){
            String question = (String) questions.get(i);
            if(question.contains(input)){
                answerIndex.add(i);
                System.out.println("문제 : " + j);
                System.out.println(question);
                j++;
            }
        }

        return answerIndex;
    }

    public static String readInput(){

        Scanner scanner = new Scanner(System.in);

        System.out.println("**(만약 종료를 원하시면 q나 quit을 입력하십시오)**");

        System.out.print("원하는 키워드를 입력하시오 ex)금리, 효용 : ");
        String keyword = scanner.nextLine();

        if(keyword.equals("Q") || keyword.equals("q") || keyword.equals("quit") || keyword.equals("QUIT") || keyword.equals("Quit")){
            quit();
        }

        return keyword;
    }

    public static ArrayList<String>[] saveProblemAndAnswer(String downloadDir) {

        HWPFile hwpFile = null;
        ArrayList<String>[] lists = new ArrayList[2];
        ArrayList<String> MockQuestions = new ArrayList<String>();
        ArrayList<String> questions = new ArrayList<String>();
        ArrayList<String> answers = new ArrayList<String>();
        ArrayList<Integer> questionStartPosition = new ArrayList<Integer>();
        String hwpText = "";

        try {
            hwpFile = HWPReader.fromFile(downloadDir + "//" + "매경테스트 기출문제집.DEA.hwp");
        } catch (Exception e) {
            System.out.println("모의고사 파일을 찾을 수 없습니다.");
            quit();
        }

        try {
            hwpText = TextExtractor.extract(hwpFile, TextExtractMethod.InsertControlTextBetweenParagraphText);
        } catch (UnsupportedEncodingException e) {
            System.out.println("파일을 읽어올 수 없습니다.");
            quit();
        }

        Pattern questionPattern = Pattern.compile("^*\\D[0-9]{3}\\.");
        Matcher questionMatcher = questionPattern.matcher(hwpText);

        while(questionMatcher.find()) {
            questionStartPosition.add(questionMatcher.end());
        }

        for(int i = 0; i < questionStartPosition.size() - 1; i++){
            String question = "";

            for(int j = questionStartPosition.get(i) + 1; j < questionStartPosition.get(i+1) - 4; j++){
                question += hwpText.charAt(j);
            }
            MockQuestions.add(question);
        }

        for(int i = 0; i < MockQuestions.size(); i++) {

            Pattern answerPattern = Pattern.compile("해  설");
            Matcher answerMatcher = answerPattern.matcher(MockQuestions.get(i));
            int lastIndex = 0;

            if(answerMatcher.find()){
                lastIndex = answerMatcher.end();
                String question = "";
                String answer = "";

                for(int j = 0; j < lastIndex - 4; j++){
                    question += MockQuestions.get(i).charAt(j);
                }

                for(int j = lastIndex - 4; j < MockQuestions.get(i).length(); j++) {
                    answer += MockQuestions.get(i).charAt(j);
                }

                questions.add(question);
                answers.add(answer);
            }
        }

        lists[0] = questions;
        lists[1] = answers;

        return lists;
    }


    public static int fileUrlReadAndDownload(String fileUrl, String localFileName, String downloadDir) {
        OutputStream outStream = null;
        URLConnection uCon = null;

        InputStream is = null;
        int byteWritten = 0;
        try {

            URL Url;
            byte[] buf;
            int byteRead;
            Url = new URL(fileUrl);
            outStream = new BufferedOutputStream(
                    new FileOutputStream(downloadDir + File.separator + URLDecoder.decode(
                            localFileName, "UTF-8")));

            uCon = Url.openConnection();
            is = uCon.getInputStream();
            buf = new byte[bufferSize];
            while ((byteRead = is.read(buf)) != -1) {
                outStream.write(buf, 0, byteRead);
                byteWritten += byteRead;
            }

            System.out.println("성공적으로 다운로드 되었습니다.");
            System.out.println("파일 이름: " + localFileName);
            System.out.println("바이트: " + byteWritten);
            System.out.println("-------다운로드 종료--------");

        } catch (Exception e) {
            System.out.println("인터넷에 연결이 되어 있지 않습니다.");
            System.out.println("인터넷에 연결한 후 다시 이용해 주십시오.");
            quit();
        } finally {
            try {
                is.close();
                outStream.close();
            } catch (IOException e) {
                System.out.println("입출력에 문제가 생겼습니다. ");
                quit();
            }
        }
        return byteWritten;
    }
}
