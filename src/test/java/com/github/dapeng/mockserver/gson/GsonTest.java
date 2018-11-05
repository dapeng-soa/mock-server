package com.github.dapeng.mockserver.gson;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.TypeAdapter;
import com.google.gson.stream.JsonReader;
import com.google.gson.stream.JsonWriter;

import java.io.IOException;

/**
 * @author <a href=mailto:leihuazhe@gmail.com>maple</a>
 * @since 2018-11-03 12:17 AM
 */
public class GsonTest {

    private static final Gson gson = new Gson();
    /**
     * gson
     */
    private static final Gson gsonAdapter = new GsonBuilder().registerTypeAdapter(Person.class, new TypeAdapter<Person>() {

        @Override
        public void write(JsonWriter writer, Person person) throws IOException {
            writer.beginObject();
            writer.name("age").value(person.getAge());
            writer.name("describe").value(person.getDescribe());
            writer.name("name").value(person.getName());
            writer.name("nickName").value(person.getNickName());
            writer.endObject();
        }

        @Override
        public Person read(JsonReader in) throws IOException {
            return null;
        }
    }).create();


    public static void main(String[] args) {
        Person p = new Person("Lei Huazhe", "maple", 26, "我是一名代码发烧友!!!");
        String res1 = gson.toJson(p);
        String res2 = gsonAdapter.toJson(p);

        System.out.println(res1);
        System.out.println("\n\n");
        System.out.println(res2);

        System.out.println("\n\n\n\n");


        Person person = gson.fromJson(res1, Person.class);

        System.out.println(person);
    }
}
