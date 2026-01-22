import * as React from 'react';
import Table from '@mui/material/Table';
import TableBody from '@mui/material/TableBody';
import TableCell from '@mui/material/TableCell';
import TableContainer from '@mui/material/TableContainer';
import TableHead from '@mui/material/TableHead';
import TableRow from '@mui/material/TableRow';
import Paper from '@mui/material/Paper';
import { IconButton } from '@mui/material';
import DeleteIcon from '@mui/icons-material/Delete';
import EditIcon from '@mui/icons-material/Edit';


export default function ProductsTable({ filter, products, deleteProduct, editProduct }) {

  const filteredProducts = products.filter(product =>
    filter === '' || product.name.toLowerCase().includes(filter.toLowerCase())
  );
  console.log(filteredProducts);

  return (
    <TableContainer component={Paper}>
      <Table sx={{ minWidth: 650 }} aria-label="simple table">
        <TableHead>
          <TableRow>
            <TableCell>id</TableCell>
            <TableCell align="right" id="productsTableHeaderName">name</TableCell>
            <TableCell align="right" id="productsTableHeaderMax">maxMeasure</TableCell>
            <TableCell align="right" id="productsTableHeaderMin">minMeasure</TableCell>
            <TableCell align="right" id="productsTableHeaderEdit">edit</TableCell>
            <TableCell align="right" id="productsTableHeaderDelete">delete</TableCell>
          </TableRow>
        </TableHead>
        <TableBody>
          {filteredProducts.map((row) => (
            <TableRow
              key={row.id}
              sx={{ '&:last-child td, &:last-child th': { border: 0 } }}
            >
              <TableCell component="th" scope="row">
                {row.id}
              </TableCell>
              <TableCell align="right" id={`productsTableBodyName${row.id}`} name="productsTableBodyName">{row.name}</TableCell>
              <TableCell align="right" id={`productsTableBodyMax${row.id}`} name="productsTableBodyName">{row.maxMeasure}</TableCell>
              <TableCell align="right" id={`productsTableBodyMin${row.id}`} name="productsTableBodyName">{row.minMeasure}</TableCell>
              <TableCell align="right" id={`productsTableBodyEdit${row.id}`} name="productsTableBodyName">
                <IconButton onClick={() => editProduct(row.id)} id={`productsTableBodyEdit${row.id}`}>
                  <EditIcon />
                </IconButton>
              </TableCell>
              <TableCell align="right">
                <IconButton onClick={() => deleteProduct(row.id)} id={`productsTableBodyDelete${row.id}`}>
                  <DeleteIcon />
                </IconButton>
              </TableCell>
            </TableRow>
          ))}
        </TableBody>
      </Table>
    </TableContainer>
  );
}