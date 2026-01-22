import axios from 'axios';

const axiosBase = axios.create({
    baseURL: process.env.REACT_APP_BACKEND_URL,
    timeout: 5000,
    headers: {
        "Content-Type": "application/json",
        Accept: "application/json",
    },
});
export const measurementsApi = {
    //measurements
    getHistory: () => {
        return axiosBase.get("/history");
    },
    postMeasurement: (id, avgTemperature) => {
        return axiosBase.post("/product_measurement", {
            id: id,
            avgTemperature: avgTemperature
        });
    },
    //products
    getProducts: () => {
        return axiosBase.get("/products");
    },
    getProductById: (id) => {
        return axiosBase.get(`/products/${id}`);
    },
    postProduct: (name, max, min) => {
        return axiosBase.post("/products", {
            name: name,
            maxMeasure: max,
            minMeasure: min
        });
    },
    putProduct: (id, name, max, min) => {
        return axiosBase.put(`/products/${id}`, {
            name: name,
            maxMeasure: max,
            minMeasure: min
        });
    },
    deleteProduct: (id) => {
        return axiosBase.delete(`/products/${id}`);
    }
}

export class MeasurementsApi {
    constructor() {
        this.client = axios.create({
            baseURL: process.env.REACT_APP_BACKEND_URL,
            timeout: 5000,
            headers: {
                "Content-Type": "application/json",
                Accept: "application/json",
            },
        });
    }
    //measurements
    getHistory = () => {
        return this.client.get("/history");
    }
    postMeasurement = (id, avgTemperature) => {
        return this.client.post("/product_measurement", {
            id: id,
            avgTemperature: avgTemperature
        });
    }
    //products
    getProducts = () => {
        return this.client.get("/products");
    }
    getProductById = (id) => {
        return this.client.get(`/products/${id}`);
    }
    postProduct = (name, max, min) => {
        return this.client.post("/products", {
            name: name,
            maxMeasure: max,
            minMeasure: min
        });
    }
    putProduct = (id, name, max, min) => {
        return this.client.put(`/products/${id}`, {
            name: name,
            maxMeasure: max,
            minMeasure: min
        });
    }
    deleteProduct = (id) => {
        return this.client.delete(`/products/${id}`);
    }
}
