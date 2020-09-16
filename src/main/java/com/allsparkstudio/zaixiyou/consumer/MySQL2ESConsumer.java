package com.allsparkstudio.zaixiyou.consumer;

import com.allsparkstudio.zaixiyou.pojo.po.Circle;
import com.allsparkstudio.zaixiyou.pojo.po.Post;
import com.allsparkstudio.zaixiyou.pojo.po.User;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import lombok.extern.slf4j.Slf4j;
import org.elasticsearch.action.delete.DeleteRequest;
import org.elasticsearch.action.delete.DeleteResponse;
import org.elasticsearch.action.index.IndexRequest;
import org.elasticsearch.action.index.IndexResponse;
import org.elasticsearch.action.update.UpdateRequest;
import org.elasticsearch.action.update.UpdateResponse;
import org.elasticsearch.client.RequestOptions;
import org.elasticsearch.client.RestHighLevelClient;
import org.elasticsearch.common.xcontent.XContentType;
import org.springframework.amqp.rabbit.annotation.Exchange;
import org.springframework.amqp.rabbit.annotation.Queue;
import org.springframework.amqp.rabbit.annotation.QueueBinding;
import org.springframework.amqp.rabbit.annotation.RabbitListener;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Component;

import java.io.IOException;


@Component
@Slf4j
public class MySQL2ESConsumer {

    private static final Gson gson = new GsonBuilder().setDateFormat("yyyy-MM-dd HH:mm:ss").create();

    @Autowired
    @Qualifier("restHighLevelClient")
    private RestHighLevelClient client;

    @RabbitListener(bindings = {
            @QueueBinding(
                    // 创建临时队列
                    value = @Queue(),
                    // 指定交换机
                    exchange = @Exchange(name = "MySQL2ESPostExchange"),
                    // 路由key：只接收更新活跃用户的信息，验证token成功时的后续处理
                    key = {"add"}
            )})
    public void addPost(Post post) throws IOException {
        IndexRequest request = new IndexRequest("post");
        request.id(String.valueOf(post.getId()));
        request.source(gson.toJson(post), XContentType.JSON);

        IndexResponse response = client.index(request, RequestOptions.DEFAULT);
        log.debug("ES插入post数据 response:[{}]", response.toString());
    }

    @RabbitListener(bindings = {
            @QueueBinding(
                    // 创建临时队列
                    value = @Queue(),
                    // 指定交换机
                    exchange = @Exchange(name = "MySQL2ESPostExchange"),
                    // 路由key：只接收更新活跃用户的信息，验证token成功时的后续处理
                    key = {"update"}
            )})
    public void updatePost(Post post) throws IOException {
        UpdateRequest request = new UpdateRequest("post", String.valueOf(post.getId()));
        request.doc(gson.toJson(post), XContentType.JSON);

        UpdateResponse response = client.update(request, RequestOptions.DEFAULT);
        log.debug("ES更新post数据 response:[{}]", response.toString());
    }

    @RabbitListener(bindings = {
            @QueueBinding(
                    // 创建临时队列
                    value = @Queue(),
                    // 指定交换机
                    exchange = @Exchange(name = "MySQL2ESPostExchange"),
                    // 路由key：只接收更新活跃用户的信息，验证token成功时的后续处理
                    key = {"delete"}
            )})
    public void deletePost(Post post) throws IOException {
        DeleteRequest request = new DeleteRequest("post",String.valueOf(post.getId()));
        DeleteResponse response = client.delete(request, RequestOptions.DEFAULT);
        log.debug("ES删除post数据 response:[{}]", response.toString());
    }

    @RabbitListener(bindings = {
            @QueueBinding(
                    // 创建临时队列
                    value = @Queue(),
                    // 指定交换机
                    exchange = @Exchange(name = "MySQL2ESUserExchange"),
                    // 路由key：只接收更新活跃用户的信息，验证token成功时的后续处理
                    key = {"add"}
            )})
    public void addUser(User user) throws IOException {
        IndexRequest request = new IndexRequest("post");
        request.id(String.valueOf(user.getId()));
        request.source(gson.toJson(user), XContentType.JSON);

        IndexResponse response = client.index(request, RequestOptions.DEFAULT);
        log.debug("ES插入user数据 response:[{}]", response.toString());
    }

    @RabbitListener(bindings = {
            @QueueBinding(
                    // 创建临时队列
                    value = @Queue(),
                    // 指定交换机
                    exchange = @Exchange(name = "MySQL2ESUserExchange"),
                    // 路由key：只接收更新活跃用户的信息，验证token成功时的后续处理
                    key = {"update"}
            )})
    public void updateUser(User user) throws IOException {
        UpdateRequest request = new UpdateRequest("post", String.valueOf(user.getId()));
        request.doc(gson.toJson(user), XContentType.JSON);

        UpdateResponse response = client.update(request, RequestOptions.DEFAULT);
        log.debug("ES更新user数据 response:[{}]", response.toString());
    }

    @RabbitListener(bindings = {
            @QueueBinding(
                    // 创建临时队列
                    value = @Queue(),
                    // 指定交换机
                    exchange = @Exchange(name = "MySQL2ESUserExchange"),
                    // 路由key：只接收更新活跃用户的信息，验证token成功时的后续处理
                    key = {"delete"}
            )})
    public void deleteUser(User user) throws IOException {
        DeleteRequest request = new DeleteRequest("post",String.valueOf(user.getId()));
        DeleteResponse response = client.delete(request, RequestOptions.DEFAULT);
        log.debug("ES删除user数据 response:[{}]", response.toString());
    }

    @RabbitListener(bindings = {
            @QueueBinding(
                    // 创建临时队列
                    value = @Queue(),
                    // 指定交换机
                    exchange = @Exchange(name = "MySQL2ESCircleExchange"),
                    // 路由key：只接收更新活跃用户的信息，验证token成功时的后续处理
                    key = {"add"}
            )})
    public void addCircle(Circle circle) throws IOException {
        IndexRequest request = new IndexRequest("post");
        request.id(String.valueOf(circle.getId()));
        request.source(gson.toJson(circle), XContentType.JSON);

        IndexResponse response = client.index(request, RequestOptions.DEFAULT);
        log.debug("ES插入circle数据 response:[{}]", response.toString());
    }

    @RabbitListener(bindings = {
            @QueueBinding(
                    // 创建临时队列
                    value = @Queue(),
                    // 指定交换机
                    exchange = @Exchange(name = "MySQL2ESCircleExchange"),
                    // 路由key：只接收更新活跃用户的信息，验证token成功时的后续处理
                    key = {"update"}
            )})
    public void updateCircle(Circle circle) throws IOException {
        UpdateRequest request = new UpdateRequest("post", String.valueOf(circle.getId()));
        request.doc(gson.toJson(circle), XContentType.JSON);

        UpdateResponse response = client.update(request, RequestOptions.DEFAULT);
        log.debug("ES更新circle数据 response:[{}]", response.toString());
    }

    @RabbitListener(bindings = {
            @QueueBinding(
                    // 创建临时队列
                    value = @Queue(),
                    // 指定交换机
                    exchange = @Exchange(name = "MySQL2ESCircleExchange"),
                    // 路由key：只接收更新活跃用户的信息，验证token成功时的后续处理
                    key = {"delete"}
            )})
    public void deleteCircle(Circle circle) throws IOException {
        DeleteRequest request = new DeleteRequest("post",String.valueOf(circle.getId()));
        DeleteResponse response = client.delete(request, RequestOptions.DEFAULT);
        log.debug("ES删除circle数据 response:[{}]", response.toString());
    }

}
