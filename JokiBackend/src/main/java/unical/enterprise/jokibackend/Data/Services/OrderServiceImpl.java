package unical.enterprise.jokibackend.Data.Services;

import lombok.RequiredArgsConstructor;
import org.modelmapper.ModelMapper;
import org.springframework.stereotype.Service;
import unical.enterprise.jokibackend.Data.Dao.OrderDao;
import unical.enterprise.jokibackend.Data.Entities.Order;
import unical.enterprise.jokibackend.Data.Dto.OrderDto;

import java.util.Collection;
import java.util.UUID;

@Service
@RequiredArgsConstructor
public class OrderServiceImpl implements OrderService {

    private final OrderDao orderDao;
    private final ModelMapper modelMapper;

    @Override
    public void save(Order order) {
        orderDao.save(order);
    }

    @Override
    public void delete(Order order) {
        orderDao.delete(order);
    }

    @Override
    public void deleteById(UUID id) {
        orderDao.deleteById(id);
    }

    @Override
    public void update(Order order) {
        orderDao.save(order);
    }


    @Override
    public OrderDto getById(UUID id) {
        Order order = orderDao.findById(id).orElse(null);
        return modelMapper.map(order, OrderDto.class);
    }

    @Override
    public Collection<OrderDto> findAll() {
        return orderDao.findAll()
                .stream().map(order -> modelMapper
                .map(order, OrderDto.class))
                .toList();
    }

    @Override
    public Collection<OrderDto> getByUserId(UUID id) {
        return orderDao.findOrdersByUserId(id)
                .stream().map(order -> modelMapper
                .map(order, OrderDto.class))
                .toList();
    }

    @Override
    public Collection<OrderDto> getByGameId(UUID id) {
        return orderDao.findOrdersByGameId(id)
                .stream().map(order -> modelMapper
                .map(order, OrderDto.class))
                .toList();
    }

    @Override
    public Collection<OrderDto> getOrderByStatus(Boolean status) {
        return orderDao.findOrdersByStatus(status)
                .stream().map(order -> modelMapper
                .map(order, OrderDto.class))
                .toList();
    }

    @Override
    public Collection<OrderDto> getOrdersByGameTitle(String title) {
        return orderDao.findOrdersByGameTitle(title)
                .stream().map(order -> modelMapper
                .map(order, OrderDto.class))
                .toList();
    }
}
