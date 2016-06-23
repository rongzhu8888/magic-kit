package test;

import org.apache.kafka.clients.producer.*;
import org.apache.kafka.common.PartitionInfo;

import java.util.List;
import java.util.Properties;

/**
 * Created by zhurong on 2016-6-20.
 */
public class ProducerTest {

    public static void main(String[] args) {

        Properties props = new Properties();
        props.put(ProducerConfig.BOOTSTRAP_SERVERS_CONFIG, "112.74.84.31:9092");
        props.put(ProducerConfig.ACKS_CONFIG, "all");
        props.put(ProducerConfig.RETRIES_CONFIG, 0);
        props.put(ProducerConfig.BATCH_SIZE_CONFIG, 16384);
        props.put(ProducerConfig.LINGER_MS_CONFIG, 1);
        props.put(ProducerConfig.BUFFER_MEMORY_CONFIG, 33554432);
        props.put(ProducerConfig.KEY_SERIALIZER_CLASS_CONFIG, "org.apache.kafka.common.serialization.StringSerializer");
        props.put(ProducerConfig.VALUE_SERIALIZER_CLASS_CONFIG, "org.apache.kafka.common.serialization.StringSerializer");
        //更多参数配置详见：http://kafka.apache.org/documentation.html#producerconfigs

        Producer<String, String> producer = new KafkaProducer<>(props); //Producer是线程安全，单实例效率更高
        List<PartitionInfo> partitionInfoList = producer.partitionsFor("A10001");
        System.out.println(partitionInfoList.size());
        for(int i = 0; i < 100; i++) {
            producer.send(new ProducerRecord<String, String>("A10001", Integer.toString(i), "xxxx[" + i + "]")
                    , new Callback() {
                        @Override
                        public void onCompletion(RecordMetadata recordMetadata, Exception e) {
                            System.out.println("sent");
                        }
                    });
        }

        producer.flush(); //立即从本地缓存区发送到Kafka brokers中

        producer.close();

    }
}
