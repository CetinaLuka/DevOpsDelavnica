import * as React from 'react';
import Box from '@mui/material/Box';
import Button from '@mui/material/Button';
import Typography from '@mui/material/Typography';
import Modal from '@mui/material/Modal';
import { TextField } from '@mui/material';
import { measurementsApi } from '../../api/api';

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

export default function EditProduct({ open, handleClose, id }) {
    const [name, setName] = React.useState("");
    const [maxMeasure, setMaxMeasure] = React.useState("");
    const [minMeasure, setMinMeasure] = React.useState("");

    React.useEffect(() => {
        const fetchData = async () => {
            const result = await measurementsApi.getProductById(id);
            if (result.request.status === 200) {
                setName(result.data.name);
                setMaxMeasure(result.data.maxMeasure);
                setMinMeasure(result.data.minMeasure);
            }
        }
        if (open && id) {
            fetchData();
        }
    }, [open, id]);
    const editProduct = async () => {
        const result = await measurementsApi.putProduct(id, name, maxMeasure, minMeasure);
        if (result.request.status === 200) {
            setName("");
            setMaxMeasure("");
            setMinMeasure("");
            handleClose();
        }
    }

    return (
        <div>

            <Modal
                open={open}
                onClose={handleClose}
                id="editProductModal"
                aria-labelledby="modal-modal-title"
                aria-describedby="modal-modal-description"
            >
                <Box sx={style}>
                    <Typography id="modal-modal-title" variant="h6" component="h2">
                        Edit product
                    </Typography>
                    <br />
                    <br />
                    <TextField
                        required
                        id="editProductNameInput"
                        name="name"
                        label="product name"
                        value={name}
                        onChange={(event) => { setName(event.target.value) }}
                    />
                    <br />
                    <br />
                    <TextField
                        required
                        id="editProductMaxInput"
                        name="max"
                        label="Max measure"
                        value={maxMeasure}
                        onChange={(event) => { setMaxMeasure(event.target.value) }}
                    />
                    <br />
                    <br />
                    <TextField
                        required
                        id="editProductMinInput"
                        name="Min"
                        label="Min measure"
                        value={minMeasure}
                        onChange={(event) => { setMinMeasure(event.target.value) }}
                    />
                    <br />
                    <br />
                    <div>
                        <Button id="editProductIdButton" name="editProductIdButton" variant="contained" onClick={editProduct}>Save</Button>
                    </div>
                </Box>
            </Modal>
        </div>
    );
}