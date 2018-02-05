package org.com.yzh.framework.springmvc.servlet;

import org.com.yzh.framework.springmvc.annotation.*;
import org.com.yzh.framework.springmvc.util.Handler;
import org.com.yzh.framework.springmvc.util.BeanUtil;

import javax.servlet.*;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.lang.annotation.Annotation;
import java.lang.reflect.Field;
import java.lang.reflect.Method;
import java.net.URL;
import java.util.*;
import java.util.regex.Pattern;

/**
 * @ClassName: YzhDispatcherServlet
 * @Description: That's the purpose of the class
 * @Author: yin.zhh
 * @Date 2018-01-23 11:23
 * @Version v.1.0.0
 */
public class YzhDispatcherServlet extends HttpServlet {

    private List<String> list = new ArrayList<>();
    private Properties prop = new Properties();
    private Map<String, Object> beans = new HashMap<>(3);
    private List<Handler> handlerMapping = new ArrayList<>(1);

    /**
     * Called by the servlet container to indicate to a servlet that the
     * servlet is being placed into service.  See {@link Servlet#init}.
     * <p>
     * <p>This implementation stores the {@link ServletConfig}
     * object it receives from the servlet container for later use.
     * When overriding this form of the method, call
     * <code>super.init(config)</code>.
     *
     * @param config the <code>ServletConfig</code> object
     *               that contains configutation
     *               information for this servlet
     * @throws ServletException if an exception occurs that
     *                          interrupts the servlet's normal
     *                          operation
     * @see UnavailableException
     */
    @Override
    public void init(ServletConfig config) {
        /*1.加载配置文件*/
        doLoadConfig(config);

        /*2.包扫描*/

        doScanPackge(prop.getProperty("ScanPackege"));

        /*3.初始化,将所有相关实例加载进容器中*/
        initBeans();

        /*4.带@Atowreid注解的进行赋值(反射)*/
        initAtoreid();

        /*5.初始化HandlerMaping,将@Method中定义的内容和@Controller中Method关联并加入容器中*/
        initHandlerMaping();

    }

    /**
     * Called by the server (via the <code>service</code> method) to
     * allow a servlet to handle a GET request.
     * <p>
     * <p>Overriding this method to support a GET request also
     * automatically supports an HTTP HEAD request. A HEAD
     * request is a GET request that returns no body in the
     * response, only the request header fields.
     * <p>
     * <p>When overriding this method, read the request data,
     * write the response headers, get the response's writer or
     * output stream object, and finally, write the response data.
     * It's best to include content type and encoding. When using
     * a <code>PrintWriter</code> object to return the response,
     * set the content type before accessing the
     * <code>PrintWriter</code> object.
     * <p>
     * <p>The servlet container must write the headers before
     * committing the response, because in HTTP the headers must be sent
     * before the response body.
     * <p>
     * <p>Where possible, set the Content-Length header (with the
     * {@link ServletResponse#setContentLength} method),
     * to allow the servlet container to use a persistent connection
     * to return its response to the client, improving performance.
     * The content length is automatically set if the entire response fits
     * inside the response buffer.
     * <p>
     * <p>When using HTTP 1.1 chunked encoding (which means that the response
     * has a Transfer-Encoding header), do not set the Content-Length header.
     * <p>
     * <p>The GET method should be safe, that is, without
     * any side effects for which users are held responsible.
     * For example, most form queries have no side effects.
     * If a client request is intended to change stored data,
     * the request should use some other HTTP method.
     * <p>
     * <p>The GET method should also be idempotent, meaning
     * that it can be safely repeated. Sometimes making a
     * method safe also makes it idempotent. For example,
     * repeating queries is both safe and idempotent, but
     * buying a product online or modifying data is neither
     * safe nor idempotent.
     * <p>
     * <p>If the request is incorrectly formatted, <code>doGet</code>
     * returns an HTTP "Bad Request" message.
     *
     * @param req  an {@link HttpServletRequest} object that
     *             contains the request the client has made
     *             of the servlet
     * @param resp an {@link HttpServletResponse} object that
     *             contains the response the servlet sends
     *             to the client
     * @throws IOException      if an input or output error is
     *                          detected when the servlet handles
     *                          the GET request
     * @throws ServletException if the request for the GET
     *                          could not be handled
     * @see ServletResponse#setContentType
     */
    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws IOException {
        this.doPost(req, resp);
    }

    /**
     * Called by the server (via the <code>service</code> method)
     * to allow a servlet to handle a POST request.
     * <p>
     * The HTTP POST method allows the client to send
     * data of unlimited length to the Web server a single time
     * and is useful when posting information such as
     * credit card numbers.
     * <p>
     * <p>When overriding this method, read the request data,
     * write the response headers, get the response's writer or output
     * stream object, and finally, write the response data. It's best
     * to include content type and encoding. When using a
     * <code>PrintWriter</code> object to return the response, set the
     * content type before accessing the <code>PrintWriter</code> object.
     * <p>
     * <p>The servlet container must write the headers before committing the
     * response, because in HTTP the headers must be sent before the
     * response body.
     * <p>
     * <p>Where possible, set the Content-Length header (with the
     * {@link ServletResponse#setContentLength} method),
     * to allow the servlet container to use a persistent connection
     * to return its response to the client, improving performance.
     * The content length is automatically set if the entire response fits
     * inside the response buffer.
     * <p>
     * <p>When using HTTP 1.1 chunked encoding (which means that the response
     * has a Transfer-Encoding header), do not set the Content-Length header.
     * <p>
     * <p>This method does not need to be either safe or idempotent.
     * Operations requested through POST can have side effects for
     * which the user can be held accountable, for example,
     * updating stored data or buying items online.
     * <p>
     * <p>If the HTTP POST request is incorrectly formatted,
     * <code>doPost</code> returns an HTTP "Bad Request" message.
     *
     * @param req  an {@link HttpServletRequest} object that
     *             contains the request the client has made
     *             of the servlet
     * @param resp an {@link HttpServletResponse} object that
     *             contains the response the servlet sends
     *             to the client
     * @throws IOException      if an input or output error is
     *                          detected when the servlet handles
     *                          the request
     * @throws ServletException if the request for the POST
     *                          could not be handled
     * @see ServletOutputStream
     * @see ServletResponse#setContentType
     */
    @Override
    protected void doPost(HttpServletRequest req, HttpServletResponse resp) throws IOException {
        //通过用户请求的URL,找到对应的method,通过反射调用,并返回其结果
        doDisPatch(req, resp);
    }

    private void doDisPatch(HttpServletRequest req, HttpServletResponse resp) throws IOException {

        try {
            //从上面已经初始化的信息中匹配,拿着用户请求url去找到其对应的Method
            boolean isMatcher = BeanUtil.invoke(req, resp, handlerMapping);
            if (!isMatcher) {
                resp.getWriter().write("404 Not Found!");
            }
        } catch (Exception e) {
            resp.getWriter().write("500 Exception,Details:\r\n" + e.getMessage() + "\r\n" +
                    Arrays.toString(e.getStackTrace()).replaceAll("\\[]", "").replaceAll(",\\s", "\r\n"));
        }
    }

    private void doLoadConfig(ServletConfig config) {
        String configParameter = config.getInitParameter("contextConfigLocation");
        if (configParameter == null) {
            throw new NullPointerException("    contextConfigLocation is empty!");
        }

        InputStream resourceAsStream = this.getClass().getClassLoader().getResourceAsStream(configParameter);

        try {
            prop.load(resourceAsStream);
        } catch (IOException e) {
            e.printStackTrace();
        } finally {
            if (resourceAsStream != null) {
                try {
                    resourceAsStream.close();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        }
    }


    private void initAtoreid() {
        if (beans.isEmpty()) {
            return;
        }

        for (Map.Entry<String, Object> bean : beans.entrySet()) {

            Field[] fields = bean.getValue().getClass().getDeclaredFields();

            try {
                for (Field field : fields) {

                    if (!field.isAnnotationPresent(Autowired.class)) {
                        continue;
                    }
                    Autowired autowired = field.getAnnotation(Autowired.class);

                    field.setAccessible(true);

                    String beanName = autowired.value().trim();
                    if ("".equals(beanName)) {
                        beanName = field.getType().getName();
                    }
                    field.set(bean.getValue(), beans.get(beanName));
                }
            } catch (Exception e) {
                e.printStackTrace();
            }
        }

    }

    private void initBeans() {
        if (list.isEmpty()) {
            return;
        }
        try {
            for (String className : list) {
                Class<?> clazz = Class.forName(className);

                if (clazz.isAnnotationPresent(Controller.class)) {
                    Object objcet = clazz.newInstance();

                    beans.put(BeanUtil.toLowerFirst(clazz.getSimpleName()), objcet);

                } else if (clazz.isAnnotationPresent(Service.class)) {

                    Service service = clazz.getAnnotation(Service.class);
                    String value = service.value().trim();
                    Object objcet = clazz.newInstance();

                    if ("".equals(value)) {
                        //默认beanName,首字母小写
                        beans.put(BeanUtil.toLowerFirst(clazz.getSimpleName()), objcet);
                    } else {
                        //自定义beanName
                        beans.put(value, objcet);
                    }

                    //根据类型去判断,找出所有接口的类型
                    Class<?>[] interfaces = clazz.getInterfaces();
                    for (Class<?> anInterface : interfaces) {
                        beans.put(anInterface.getName(), objcet);
                    }

                }

            }
        } catch (Exception e) {
            e.printStackTrace();
        }


    }

    private void initHandlerMaping() {

        if (beans.isEmpty()) {
            return;
        }

        for (Map.Entry<String, Object> entry : beans.entrySet()) {

            Class<?> clazz = entry.getValue().getClass();
            if (!clazz.isAnnotationPresent(Controller.class)) {
                continue;
            }

            String url = "";
            if (clazz.isAnnotationPresent(Path.class)) {
                Path requstMapping = clazz.getAnnotation(Path.class);
                url = requstMapping.value();
            }

            Method[] methods = clazz.getMethods();
            for (Method method : methods) {

                if (!method.isAnnotationPresent(org.com.yzh.framework.springmvc.annotation.Method.class)) {
                    continue;
                }

                org.com.yzh.framework.springmvc.annotation.Method requstMapping = method.getAnnotation(org.com.yzh.framework.springmvc.annotation.Method.class);
                String customRegex = ("/" + url + "/" + requstMapping.value()).replaceAll("/+", "/");

                String regex = customRegex.replaceAll("\\*", ".*");

                Map<String, Integer> pm = new HashMap<>();

                Annotation[][] pa = method.getParameterAnnotations();
                for (int i = 0; i < pa.length; i++) {
                    for (Annotation a : pa[i]) {
                        if (a instanceof Param) {
                            String paramName = ((Param) a).value();
                            if (!"".equals(paramName.trim())) {
                                pm.put(paramName, i);
                            }
                        }
                    }
                }

                //提取Request和Response的索引
                Class<?>[] paramsTypes = method.getParameterTypes();
                for (int i = 0; i < paramsTypes.length; i++) {
                    Class<?> type = paramsTypes[i];
                    if (type == HttpServletRequest.class || type == HttpServletResponse.class) {
                        pm.put(type.getName(), i);
                    }
                }

                handlerMapping.add(new Handler(entry.getValue(), method, Pattern.compile(regex), pm));

                System.out.println("Mapping-- " + customRegex + " -- " + method);
            }
        }

    }

    private void doScanPackge(String packageName) {

        String path = packageName.replaceAll("\\.", "/");

        URL url = this.getClass().getClassLoader().getResource(path);

        assert url != null;

        File file = new File(url.getFile());

        for (File f : Objects.requireNonNull(file.listFiles())) {
            if (f.isFile()) {
                list.add(packageName + "." + f.getName().split("\\.")[0]);
            } else {
                doScanPackge(packageName + "." + f.getName());
            }
        }
    }

}
