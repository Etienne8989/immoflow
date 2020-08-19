package com.immoflow.immoflow.services;

import com.immoflow.immoflow.resource.PropertyData;

public interface PropertyParser {

    PropertyData scrapeData(String basicUrl);

}
