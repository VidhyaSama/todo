

import java.time.LocalDateTime;
import java.util.List;
import java.util.concurrent.TimeUnit;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.dao.DataAccessException;
import org.springframework.data.redis.core.RedisOperations;
import org.springframework.data.redis.core.SessionCallback;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.data.redis.core.ValueOperations;
import org.springframework.stereotype.Component;
import org.springframework.web.servlet.HandlerInterceptor;

import in.cognitivebotics.metaservice.exception.RequestNotPermittedException;



@Component
public class RedisRateLimiterInterceptor implements HandlerInterceptor {

    private static final Logger LOGGER = LoggerFactory.getLogger(RedisRateLimiterInterceptor.class);

    @Autowired
    private StringRedisTemplate stringTemplate;

    //@Value("${requests_per_minute_read}")
    private String readRequest;

    //@Value("${requests_per_minute_write}")
    private String writeRequest;

    @Override
    public boolean preHandle(HttpServletRequest request, HttpServletResponse response, Object handler)
            throws Exception {

        return isAllowed(request.getRemoteAddr(), request.getMethod());
    }


    public boolean isAllowed(String ip, String methodName) throws RequestNotPermittedException {
        final int minute = LocalDateTime.now()
                .getMinute();
        if (methodName.equals("POST") || methodName.equals("PUT") || methodName.equals("DELETE")) {
            methodName = "write";
        }

        String key = ip + methodName + ":" + minute;
        ValueOperations<String, String> operations = stringTemplate.opsForValue();
        String value = operations.get(key);
        if (value != null && Integer.parseInt(value) >= Integer.parseInt(readRequest)) {
            throw new RequestNotPermittedException("Too many Requests!! Please try later.");
        } else if (value != null && Integer.parseInt(value) >= Integer.parseInt(writeRequest)) {
            throw new RequestNotPermittedException("Too many Requests!! Please try later.");
        }

        List<Object> txResults = stringTemplate.execute(new SessionCallback<List<Object>>() {

            @Override
            public <K, V> List<Object> execute(RedisOperations<K, V> operations) throws DataAccessException {
                final StringRedisTemplate redisTemplate = (StringRedisTemplate) operations;
                final ValueOperations<String, String> valueOperations = redisTemplate.opsForValue();
                operations.multi();
                valueOperations.increment(key);
                redisTemplate.expire(key, 1, TimeUnit.MINUTES); // This will contain the
                results of all operations in // the transaction return operations.exec(); }
            }); LOGGER.info("Current request count: "+txResults.get(0)); return true;
        }

    }
		
