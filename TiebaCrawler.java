import com.sun.image.codec.jpeg.JPEGCodec;
import com.sun.image.codec.jpeg.JPEGImageEncoder;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;

import javax.imageio.ImageIO;
import java.awt.Image;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.net.URL;
import java.net.URLEncoder;
import java.util.Arrays;
import java.util.List;
import java.util.Set;
import java.util.TreeSet;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * Created by Administrator on 2016/7/11.
 */
public class TiebaCrawler implements Runnable{

    public static final String jpDir=  "e:\\goodtieba\\";
    public static final String gDir=  "e:\\tieba\\";
    public static final String jpUrl="http://tieba.baidu.com/f/good?kw=";
    public static final String gUrl="http://tieba.baidu.com/f?kw=";
    private String name;

    public TiebaCrawler(String name) {
        this.name = name;
    }

    /**
     *
     * @param args "tieba 精品 f/good?
     * @throws IOException
     */
    public static void main(String[] args) throws IOException {
        List<String> keywords = Arrays.asList("郑秀晶","黄美英","金泰妍","少女时代","新垣结衣","金泰熙");

        ExecutorService service = Executors.newCachedThreadPool();

        for (int i = 0; i < keywords.size(); i++)
            service.execute(new TiebaCrawler(keywords.get(i)));
        service.shutdown();


    }

    public static void getTiebaImage(String name) throws IOException {
        Set<String> urls = new TreeSet<String>();
        String headtitle = "http://tieba.baidu.com";
        String pattern ="/p/\\d+";
        Pattern p = Pattern.compile(pattern);

        String imageDir = jpDir+ name +"\\";
        File directory = new File(imageDir);
        if(!directory.exists())
            directory.mkdir();

        String searchWord = URLEncoder.encode(name, "utf-8");
        System.out.println("开始抓取 " + name+"吧");
        String tiebaURL = jpUrl+searchWord;

        URL u = new URL(tiebaURL);
//        HttpURLConnection http = (HttpURLConnection) u.openConnection();
//        http.setRequestMethod("GET");
//        BufferedReader br = new BufferedReader(new InputStreamReader(u.openStream()));
//        FileChannel outChannel = new FileOutputStream("urlfrontier.txt").getChannel();
//        ByteBuffer buffer = ByteBuffer.allocate(1024);
        Document doc = Jsoup.parse(u.openStream(),"utf-8","/");
        Elements links = doc.select("a[href]");
        StringBuilder sb = new StringBuilder();
        for (Element link : links) {
            String linkHref = link.attr("href");
            String title = link.text().trim();
            Matcher m = p.matcher(linkHref.trim());

            if (m.find()) {
                String nextlink =  linkHref.startsWith("http")?linkHref:headtitle+linkHref;
                urls.add(trimQ(nextlink));
            }
        }

        snatchImageFromUrlSet(urls,imageDir);
    }

    public static void snatchImageFromUrlSet(Set<String> urls,String dir) throws IOException {
        for (String item : urls) {
            snatchImage(item, dir);
        }
    }

    public static void snatchImage(String item,String dir) throws IOException {
        Pattern p  = Pattern.compile("[^/]+\\.jpg");
        URL u = new URL(item);
        Document doc = Jsoup.parse(u.openStream(),"utf-8","/");
        Elements links = doc.select("img[src]");
        for (Element link : links) {
            String imgsrc =  link.attr("src");
            if(isValidImage(imgsrc)){
                String imgname = imgsrc.substring(imgsrc.lastIndexOf("/")+1,imgsrc.indexOf(".jpg"))+".jpg";
                System.out.println(dir.substring(dir.indexOf("\\")+7)+" " +imgname);
                downloadImageFromSrc(imgsrc,dir +imgname);
            }
        }
    }

    public static void downloadImageFromSrc(String imgsrc, String imgname) throws IOException {
        URL url = new URL(imgsrc);

        Image src = null;
        try {
            src = ImageIO.read(url);
        } catch (Exception e) {
            e.printStackTrace();
            return;
        }
        if(src!=null){
            int width = src.getWidth(null);
            int height = src.getHeight(null);

            FileOutputStream out = new FileOutputStream(imgname);
            BufferedImage tag = new BufferedImage(width, height, BufferedImage.TYPE_INT_RGB);
            tag.getGraphics().drawImage(src, 0, 0, width, height, null);
            JPEGImageEncoder encoder = JPEGCodec.createJPEGEncoder(out);
            encoder.encode(tag);
            out.close();

        }

    }

    public static String trimQ(String url) {
        int index;
        if ( (index = url.indexOf("?")) != -1) {
            url = url.substring(0,index);
        }
        return url;
    }

    public static boolean isValidImage(String imgsrc) {
        return imgsrc.contains("http://imgsrc.baidu.com/forum/w");
    }

    public static boolean isFutileImage(String imgsrc) {
        return imgsrc.contains("head_")
                || imgsrc.contains("image_emoticon")
                || imgsrc.contains("png")
                ||imgsrc.contains("h.hiphotos")
                ||imgsrc.contains("tb.himg");
    }

    @Override
    public void run() {
        try {
            getTiebaImage(TiebaCrawler.this.name);
        } catch (IOException e) {
            e.printStackTrace();
        }

    }


}
