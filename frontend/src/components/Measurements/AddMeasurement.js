import * as React from 'react';
import Box from '@mui/material/Box';
import Button from '@mui/material/Button';
import Typography from '@mui/material/Typography';
import Modal from '@mui/material/Modal';
import { TextField } from '@mui/material';
import { MeasurementsApi, Prod } from '../../api/api';
import { useEffect } from 'react';
import InputLabel from '@mui/material/InputLabel';
import MenuItem from '@mui/material/MenuItem';
import FormControl from '@mui/material/FormControl';
import Select, { SelectChangeEvent } from '@mui/material/Select';

const style = {
    position: 'absolute',
    top: '50%',
    left: '50%',
    transform: 'translate(-50%, -50%)',
    width: 400,
    bgcolor: 'background.paper',
    border: '2px solid #000',
    boxShadow: 24,
    p: 4,
};

export default function AddMeasurement({ open, handleClose }) {
    const [products, setProducts] = React.useState([]);
    const [productId, setProductId] = React.useState("");
    const [avgTemp, setAvgTemp] = React.useState("");
    const measurementsApi = new MeasurementsApi();
    const addMeasurement = async () => {
        const result = await measurementsApi.postMeasurement(productId, avgTemp);
        if (result.request.status === 200) {
            handleClose();
            setProductId("");
            setAvgTemp("");
        }
        console.log(result);
    }

    useEffect(() => {
        const getProducts = async () => {
            const response = await measurementsApi.getProducts();
            setProducts(response.data);
        };
        getProducts();
    }, []);

    const handleChange = (event) => {
        setProductId(event.target.value);
    };

    return (
        <div>

            <Modal
                open={open}
                onClose={handleClose}
                aria-labelledby="modal-modal-title"
                aria-describedby="modal-modal-description"
            >
                <Box sx={style}>
                    <Typography id="modal-modal-title" variant="h6" component="h2">
                        Add new measurement
                    </Typography>
                    <FormControl fullWidth>
                        <InputLabel id="demo-simple-select-label">Product</InputLabel>
                        <Select
                            labelId="demo-simple-select-label"
                            id="demo-simple-select"
                            label="Product"
                            onChange={handleChange}
                        >
                            {
                                products.map((product) => {
                                    return (
                                        <MenuItem value={product.id}>{product.name}</MenuItem>
                                    )
                                })
                            }
                        </Select>
                    </FormControl>
                    <TextField
                        required
                        id="avgTemp"
                        name="addMeasurementAvgTemp"
                        label="average temperature"
                        value={avgTemp}
                        onChange={(event) => { setAvgTemp(event.target.value) }}
                    />
                    <div>
                        <Button variant="contained" id="submitMeasurementButton" data-cy="submitMeasurementButton" onClick={addMeasurement}>Add</Button>
                    </div>
                </Box>
            </Modal>
        </div>
    );
}