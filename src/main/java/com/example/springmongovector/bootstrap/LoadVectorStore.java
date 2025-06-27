package com.example.springmongovector.bootstrap;



import com.example.springmongovector.config.VectorStoreProperties;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.ai.document.Document;
import org.springframework.ai.reader.tika.TikaDocumentReader;
import org.springframework.ai.transformer.splitter.TextSplitter;
import org.springframework.ai.transformer.splitter.TokenTextSplitter;
import org.springframework.ai.vectorstore.VectorStore;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.CommandLineRunner;
import org.springframework.stereotype.Component;

import java.util.List;

/**
 * Created by jt, Spring Framework Guru.
 */

@Component
public class LoadVectorStore implements CommandLineRunner {

    private final VectorStore vectorStore;
    private final VectorStoreProperties vectorStoreProperties;

    public LoadVectorStore(VectorStore vectorStore, VectorStoreProperties vectorStoreProperties) {
        this.vectorStore = vectorStore;
        this.vectorStoreProperties = vectorStoreProperties;
    }

    Logger logger = LoggerFactory.getLogger(LoadVectorStore.class);

    @Override
    public void run(String... args) throws Exception {

        if (vectorStore.similaritySearch("Hartford").isEmpty()){
            logger.debug("Loading documents into vector store");

            vectorStoreProperties.getDocumentsToLoad().forEach(document -> {
                System.out.println("Loading document: " + document.getFilename());

                TikaDocumentReader documentReader = new TikaDocumentReader(document);
                List<Document> documents = documentReader.get();

                TextSplitter textSplitter = new TokenTextSplitter();

                List<Document> splitDocuments = textSplitter.apply(documents);

                vectorStore.add(splitDocuments);
            });
        }

        logger.debug("Vector store loaded");
    }
}



















