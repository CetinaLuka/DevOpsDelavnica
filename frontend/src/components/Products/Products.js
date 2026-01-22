import { Button, CircularProgress, OutlinedInput, InputAdornment, IconButton, FormControl, InputLabel } from '@mui/material';
import SearchOutlinedIcon from '@mui/icons-material/SearchOutlined';
import React, { useEffect, useState } from 'react';
import { measurementsApi } from '../../api/api';
import AddProduct from './AddProduct';
import EditProduct from './EditProduct';
import ProductsTable from './ProductsTable';

const Products = () => {
    const [products, setProducts] = useState([]);
    const [filter, setFilter] = useState("");
    const [open, setOpen] = useState(false);
    const [editOpen, setEditOpen] = useState(false);
    const [editId, setEditId] = useState("");
    const [loaded, setLoaded] = useState(false);

    const fetchData = () => {
        measurementsApi.getProducts()
            .then((result) => { setProducts(result.data); setLoaded(true); })
            .catch((response) => console.log(`error ${response}`));
    }
    useEffect(() => {
        fetchData();

    }, []);
    const handleOpen = () => {
        setOpen(true);
    }
    const handleClose = () => {
        setOpen(false);
        setEditOpen(false);
        setEditId("");
        fetchData();
    }
    const deleteProduct = async (id) => {
        await measurementsApi.deleteProduct(id);
        fetchData();
    }
    const editProduct = (id) => {
        setEditId(id);
        setEditOpen(true);
    }
    const enterPressed = (event) => {
        if (event.key === 'Enter') {
            setFilter(event.target.value);
            console.log(event.target.value);
        }

    }
    return (
        <div style={{ padding: '25px' }}>
            <h2>Products</h2>
            <Button variant="contained" id="addNewProductButton" onClick={handleOpen}>New Product</Button>
            <div style={{ float: "right" }}>
                <FormControl sx={{ m: 1, width: '35ch' }} variant="outlined">
                    <InputLabel htmlFor="productSearchBox">Search</InputLabel>
                    <OutlinedInput
                        id="productSearchBox"
                        type='text'
                        onKeyDown={enterPressed}
                        endAdornment={
                            <InputAdornment position="end">
                                <SearchOutlinedIcon />
                            </InputAdornment>
                        }
                        label="Search"
                    />
                </FormControl>
            </div>

            <br />
            <br />
            {loaded ? <ProductsTable filter={filter} products={products} deleteProduct={deleteProduct} editProduct={editProduct} /> : <CircularProgress />}
            <AddProduct open={open} handleClose={handleClose} />
            <EditProduct open={editOpen} handleClose={handleClose} id={editId} />
        </div>
    );
}

export default Products;